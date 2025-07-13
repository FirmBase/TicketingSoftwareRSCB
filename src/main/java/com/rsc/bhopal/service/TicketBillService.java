package com.rsc.bhopal.service;

import java.math.BigInteger;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rsc.bhopal.dtos.ApplicationConstantDTO;
import com.rsc.bhopal.dtos.BillSummarize;
import com.rsc.bhopal.dtos.ParkingCalDTO;
import com.rsc.bhopal.dtos.TicketBillDTO;
import com.rsc.bhopal.dtos.TicketSelectorDTO;
import com.rsc.bhopal.entity.RSCUser;
import com.rsc.bhopal.entity.TicketBill;
import com.rsc.bhopal.entity.TicketBillRow;
import com.rsc.bhopal.entity.TicketsRatesMaster;
import com.rsc.bhopal.entity.VisitorsType;
import com.rsc.bhopal.exception.SerialRangeOutOfRangeException;
import com.rsc.bhopal.exception.TicketRateNotMaintainedException;
import com.rsc.bhopal.repos.TicketBillRepository;
import com.rsc.bhopal.utills.CommonUtills;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TicketBillService {

	@Autowired
	TicketBillRepository generatedTicketRepo;

	@Autowired
	private TicketsRatesService ticketsRatesService;

	@Autowired
	private RSCUserDetailsService userDetailsService;

	@Autowired
	private VisitorTypeService visitorTypeService;

	@Autowired
	private BillCalculatorService billCalculator;

	@Autowired
	private ApplicationConstantService applicationConstantService;

	public TicketBillDTO findById(long id) {
		TicketBillDTO ticketBillDTO = new TicketBillDTO();
		Optional<TicketBill> ticketBill = generatedTicketRepo.findById(id);
		if (ticketBill.isPresent()) {
			RSCUser rscUser = new RSCUser();
			BeanUtils.copyProperties(ticketBill.get().getGeneratedBy(), rscUser);
			ticketBillDTO.setGeneratedBy(rscUser.getUsername());
			// ticketBillDTO.setBillSummarize(ticketBill.get().getBillSummary());
			BillSummarize billSummarize = CommonUtills.convertJSONToObject(ticketBill.get().getTicketPayload(), BillSummarize.class);
			ticketBillDTO.setBillSummarize(billSummarize);
			BeanUtils.copyProperties(ticketBill.get(), ticketBillDTO);
		}
		return ticketBillDTO;
	}

	public List<TicketBillDTO> getAllTicketsBill() {
		List<TicketBillDTO> ticketBillDTOs = new ArrayList<TicketBillDTO>();
		generatedTicketRepo.findAll().forEach(generatedTicket -> {
			TicketBillDTO ticketBillDTO = new TicketBillDTO();
			BeanUtils.copyProperties(generatedTicket, ticketBillDTO);

			RSCUser rscUser = new RSCUser();
			BeanUtils.copyProperties(generatedTicket.getGeneratedBy(), rscUser);
			ticketBillDTO.setGeneratedBy(rscUser.getUsername());

			BillSummarize billSummarize = CommonUtills.convertJSONToObject(generatedTicket.getTicketPayload(), BillSummarize.class);
			ticketBillDTO.setBillSummarize(billSummarize);
			ticketBillDTOs.add(ticketBillDTO);
		});
		return ticketBillDTOs;
	}

	public void cancelTicketBill(long id) {
		TicketBill ticketBill = generatedTicketRepo.findById(id).orElseThrow(() -> new RuntimeException("Ticket Bill not found"));
		try {
			final BillSummarize billSummarize = CommonUtills.convertJSONToObject(ticketBill.getTicketPayload(), BillSummarize.class);
			ticketBill.setCancelledStatus(true);	// 'cancelled_status' i.e. column
			billSummarize.setCancelledStatus(true);	// BillSummarize i.e. JSON
			ticketBill.setTicketPayload(CommonUtills.convertToJSON(billSummarize));
			generatedTicketRepo.save(ticketBill);
		}
		catch(JsonProcessingException ex) {
			log.debug("Exception on ticket cancelling: ", ex.getMessage());
		}
	}

	public List<TicketBillDTO> getRecentTickets(int rows) {
		List<TicketBillDTO> ticketDTOs = new ArrayList<TicketBillDTO>();
		Pageable page = PageRequest.of(0, rows, Sort.by(Direction.DESC, "generatedAt"));
		List<TicketBill> tickets = generatedTicketRepo.recentRecords(page);
		tickets.forEach(ticket -> {
			TicketBillDTO ticketDTO = new TicketBillDTO();
			BeanUtils.copyProperties(ticket, ticketDTO);
			ticketDTO.setBillSeries(ticket.getSeries());
			ticketDTO.setBillSerial(ticket.getSerialNo());
			ticketDTO.setGeneratedBy(ticket.getGeneratedBy().getName());
			ticketDTO.setBillSummarize(ticket.getTicketPayload() != null ? CommonUtills.convertJSONToObject(ticket.getTicketPayload(), BillSummarize.class) : null);
			ticketDTOs.add(ticketDTO);
		});
		return ticketDTOs;
	}

	public List<TicketBillDTO> getTicketBills(Character billSeries, BigInteger billSerial) {
		List<TicketBillDTO> ticketBillDTOs = new ArrayList<TicketBillDTO>();
		generatedTicketRepo.getTicketBills(billSeries, billSerial).forEach(ticketBill -> {
			TicketBillDTO ticketBillDTO = new TicketBillDTO();
			BeanUtils.copyProperties(ticketBill, ticketBillDTO);
			ticketBillDTO.setBillSeries(ticketBill.getSeries());
			ticketBillDTO.setBillSerial(ticketBill.getSerialNo());
			ticketBillDTO.setGeneratedBy(ticketBill.getGeneratedBy().getName());
			ticketBillDTO.setBillSummarize(ticketBill.getTicketPayload() != null ? CommonUtills.convertJSONToObject(ticketBill.getTicketPayload(), BillSummarize.class) : null);
			ticketBillDTOs.add(ticketBillDTO);
		});
		return ticketBillDTOs;
	}

	public List<TicketBillDTO> getTicketBills(Date billDate) {
		List<TicketBillDTO> ticketBillDTOs = new ArrayList<TicketBillDTO>();
		generatedTicketRepo.getTicketBills(new java.sql.Date(billDate.getTime())).forEach(ticketBill -> {
			TicketBillDTO ticketBillDTO = new TicketBillDTO();
			BeanUtils.copyProperties(ticketBill, ticketBillDTO);
			ticketBillDTO.setBillSeries(ticketBill.getSeries());
			ticketBillDTO.setBillSerial(ticketBill.getSerialNo());
			ticketBillDTO.setGeneratedBy(ticketBill.getGeneratedBy().getName());
			ticketBillDTO.setBillSummarize(ticketBill.getTicketPayload() != null ? CommonUtills.convertJSONToObject(ticketBill.getTicketPayload(), BillSummarize.class) : null);
			ticketBillDTOs.add(ticketBillDTO);
		});
		return ticketBillDTOs;
	}

	public List<TicketBillDTO> getTicketBills(Date billDateFrom, Date billDateTo) {
		List<TicketBillDTO> ticketBillDTOs = new ArrayList<TicketBillDTO>();
		generatedTicketRepo.getTicketBills(new java.sql.Date(billDateFrom.getTime()), new java.sql.Date(billDateTo.getTime())).forEach(ticketBill -> {
			TicketBillDTO ticketBillDTO = new TicketBillDTO();
			BeanUtils.copyProperties(ticketBill, ticketBillDTO);
			ticketBillDTO.setBillSeries(ticketBill.getSeries());
			ticketBillDTO.setBillSerial(ticketBill.getSerialNo());
			ticketBillDTO.setGeneratedBy(ticketBill.getGeneratedBy().getName());
			ticketBillDTO.setBillSummarize(ticketBill.getTicketPayload() != null ? CommonUtills.convertJSONToObject(ticketBill.getTicketPayload(), BillSummarize.class) : null);
			ticketBillDTOs.add(ticketBillDTO);
		});
		return ticketBillDTOs;
	}

	@Transactional
	public Long saveAndPrintTicket(TicketSelectorDTO dto, Principal user) throws JsonProcessingException {
		if (!applicationConstantService.checkBillSerialInRange()) {
			throw new SerialRangeOutOfRangeException("Cannot generate new bill, check Bill Serial.");
		}

		Optional<VisitorsType> comboGroup = null;
		List<TicketBillRow> billRows = new ArrayList<TicketBillRow>();
		Double totalPrice = 0d;
		final boolean IS_COMBO_CASE = dto.getFamilyGroup() != 0 ? true : false;

		if (!IS_COMBO_CASE && dto.getTickets().size() < 1) {
			throw new TicketRateNotMaintainedException("No selected tickets received by server.");
		}
		TicketBill generatedTicket = new TicketBill();
		generatedTicket.setGeneratedAt(new Date());
		generatedTicket.setInstitution(dto.getInstitution());
		generatedTicket.setRemark(dto.getRemark());
		generatedTicket.setGeneratedBy(userDetailsService.getUserByUsername(user.getName()));

		// New column that were already present in JSON
		final BigInteger ticketSerial = new BigInteger(applicationConstantService.getTicketSerial().getData());
		generatedTicket.setTicketSerial(ticketSerial);
		generatedTicket.setCancelledStatus(false);

		final Character billSeries = applicationConstantService.getBillSeries().getData().charAt(0);
		generatedTicket.setSeries(billSeries);
		final BigInteger billSerial = new BigInteger(applicationConstantService.getBillSerial().getData());
		generatedTicket.setSerialNo(billSerial);

		// Generated Ticket
		List<TicketsRatesMaster> rates = null;
		if (!IS_COMBO_CASE) {
			rates = ticketsRatesService.getTicketRateByGroup(dto.getTickets(), dto.getGroup());			
			generatedTicket.setPersons(dto.getPersons());
		}
		else {
			// log.debug("=======COMOBO CASE===================");
			comboGroup = visitorTypeService.getVisitorById(dto.getFamilyGroup());
			rates = ticketsRatesService.getRatesOfCombo(dto.getFamilyGroup());
			if (rates == null) {
				throw new TicketRateNotMaintainedException("Ticket Rate is not maintained for Combo Group.");
			}
			generatedTicket.setPersons(comboGroup.get().getFixedMembers());
			dto.setPersons(generatedTicket.getPersons());
		}

		generatedTicket = generatedTicketRepo.save(generatedTicket);

		for(TicketsRatesMaster rate: rates) {
			TicketBillRow billRow = new TicketBillRow();
			if (!IS_COMBO_CASE) {
				billRow.setTotalSum(dto.getPersons() * rate.getPrice());
				totalPrice += billRow.getTotalSum();
			}
			else {
				billRow.setTotalSum(rate.getPrice());
				totalPrice += billRow.getTotalSum();
			}
			billRow.setRate(rate);
			billRow.setGeneratedTicket(generatedTicket);
			billRows.add(billRow);
		}

		if (dto.getParkings() != null) {
			for (ParkingCalDTO parking: dto.getParkings().stream().filter(parkingDTO -> parkingDTO.getCount() > 1).collect(Collectors.toList())) {
				TicketBillRow billRow = new TicketBillRow();
				final TicketsRatesMaster rate = ticketsRatesService.getActiveParkingRateFloat(parking.getId());
				// log.debug("Parking Rate: " + rate.getPrice());
				billRow.setTotalSum(parking.getCount() * rate.getPrice());
				totalPrice += billRow.getTotalSum();
				billRow.setGeneratedTicket(generatedTicket);
				billRow.setRate(rate);
				billRows.add(billRow);
			}
		}

		ApplicationConstantDTO applicationConstantDTO = applicationConstantService.getTicketSerial();
		// final BigInteger ticketSerial = new BigInteger(applicationConstantDTO.getData());
		// final BigInteger newTicketSerial = ticketSerial.add(new BigInteger("1"));
		// applicationConstantService.replaceTicketSerial(applicationConstantDTO.getId(), newTicketSerial.toString());
		applicationConstantService.replaceTicketSerial(applicationConstantDTO.getId(), ticketSerial.add(new BigInteger("1")).toString());
		applicationConstantDTO = applicationConstantService.getBillSerial();
		// BigInteger billSerial = new BigInteger(applicationConstantDTO.getData());
		applicationConstantService.replaceBillSerial(applicationConstantDTO.getId(), billSerial.add(new BigInteger("1")).toString());

		BillSummarize billSummarize = billCalculator.summarizeBill(dto);
		billSummarize.setTicketSerial(ticketSerial);
		// billSummarize.setTicketSerial(newTicketSerial);
		billSummarize.setCancelledStatus(false);
		billSummarize.setBillSeries(billSeries);
		billSummarize.setBillSerial(billSerial);
		generatedTicket.setTicketPayload(CommonUtills.convertToJSON(billSummarize));
		generatedTicket.setTotalBill(totalPrice);
		generatedTicket.setBillSummary(billRows);
		generatedTicketRepo.save(generatedTicket);
		// log.debug("generatedTicket " + CommonUtills.convertToJSON(generatedTicket));
		generatedTicketRepo.flush();
		return generatedTicket.getId();
	}
}
