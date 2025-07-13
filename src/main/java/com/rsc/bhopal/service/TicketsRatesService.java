package com.rsc.bhopal.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rsc.bhopal.dtos.NewTicketRate;
import com.rsc.bhopal.dtos.ParkingDetailsDTO;
import com.rsc.bhopal.dtos.ParkingPriceDTO;
import com.rsc.bhopal.dtos.TicketDetailsDTO;
import com.rsc.bhopal.dtos.TicketRevisedSummary;
import com.rsc.bhopal.dtos.TicketsRatesMasterDTO;
import com.rsc.bhopal.dtos.VisitorsTypeDTO;
import com.rsc.bhopal.entity.RSCUser;
import com.rsc.bhopal.entity.TicketDetails;
import com.rsc.bhopal.entity.TicketsRatesMaster;
import com.rsc.bhopal.entity.VisitorsType;
import com.rsc.bhopal.enums.BillType;
import com.rsc.bhopal.exception.TicketRateNotMaintainedException;
import com.rsc.bhopal.repos.TicketDetailsRepository;
import com.rsc.bhopal.repos.TicketsRatesMasterRepository;

import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TicketsRatesService {
	@Autowired
	private TicketsRatesMasterRepository ticketRateRepo;

	@Autowired
	private VisitorTypeService visitorTypeService;

	@Autowired
	private TicketDetailsRepository ticketDetailsRepo;

	@Autowired
	TicketDetailsService ticketDetailsService;

	@Autowired
	private RSCUserDetailsService userDetailsService;

	public List<TicketsRatesMaster> getTicketRateByGroup(List<Long> tickets, long groupId) {
		List<TicketsRatesMaster> rates = new ArrayList<TicketsRatesMaster>();
		tickets.forEach(ticket -> {
			TicketsRatesMaster  rate = ticketRateRepo.findByGroupAndTicketIds(ticket, groupId);
			if (rate == null) {
				String ticketName = ticketDetailsService.getTicketsById(ticket).get().getName();
				throw new TicketRateNotMaintainedException("Ticket Rate is not maintained for "+ticketName);
			}
			rates.add(rate);
		});
		return rates;
	}

	public List<Long> getTicketsByGroup(long familyGroupId) {
		return ticketRateRepo.getTicketsByGroup(familyGroupId);
	}

	public List<TicketRevisedSummary> getRevisedRates(){
		List<TicketRevisedSummary> dtos = new ArrayList<TicketRevisedSummary>();
		List<TicketsRatesMaster> ticketSummary = ticketRateRepo.getRecentRevisions();
		ticketSummary.forEach(ticket -> {
			TicketRevisedSummary dto = new TicketRevisedSummary();
			BeanUtils.copyProperties(ticket, dto);
			dto.setUser(ticket.getUser().getName());
			dto.setOldPrice(ticket.getOldRateMaster().getPrice());
			dto.setPriceIncreased(dto.getOldPrice()<dto.getPrice());

			if (ticket.getParkingDetails() == null) {
				dto.setTicketType(ticket.getTicketType().getName());
				dto.setVisitorsType(ticket.getVisitorsType().getName());
				dto.setParkingRate(false);
			}
			else {
				dto.setParkingRate(true);
				dto.setParkingDetails(ticket.getParkingDetails().getName());
			}
			dtos.add(dto);
		});
		return dtos;
	}

	public TicketsRatesMasterDTO getTicketRateByGroup(long ticketId, long groupId) {
		TicketDetails ticketDetails = ticketDetailsService.getTicketsById(ticketId).get();
		TicketsRatesMaster ticketRate = ticketRateRepo.findByGroupAndTicketIds(ticketId, groupId);
		if (ticketRate == null) {
			throw new TicketRateNotMaintainedException("Ticket Rate is not maintained for "+ticketDetails.getName());
		}
		TicketsRatesMasterDTO ticketRatesDTO = new TicketsRatesMasterDTO();
		TicketDetailsDTO ticketDetailsDTO = new TicketDetailsDTO();
		log.debug("TICKET {} " + ticketRate);
		BeanUtils.copyProperties(ticketDetails, ticketDetailsDTO);
		ticketRatesDTO.setTicketType(ticketDetailsDTO);

		VisitorsType visitorsType = ticketRate.getVisitorsType();
		VisitorsTypeDTO visitorsTypeDTO = new VisitorsTypeDTO();
		BeanUtils.copyProperties(visitorsType, visitorsTypeDTO);
		ticketRatesDTO.setVisitorsType(visitorsTypeDTO);

		BeanUtils.copyProperties(ticketRate, ticketRatesDTO);
		return ticketRatesDTO;
	}

	public List<TicketsRatesMasterDTO> getAllTicketRates() {
		List<TicketsRatesMasterDTO> ticketRatesDTOs = new ArrayList<>();
		List<TicketsRatesMaster> ticketRates = ticketRateRepo.findAll();

		for (TicketsRatesMaster ticketRate: ticketRates) {
			TicketsRatesMasterDTO ticketRateDTO = new TicketsRatesMasterDTO();

			TicketDetails ticketDetails = ticketRate.getTicketType();
			TicketDetailsDTO ticketDetailsDTO = new TicketDetailsDTO();
			log.debug("TICKET {} " + ticketRate);
			BeanUtils.copyProperties(ticketDetails, ticketDetailsDTO);
			ticketRateDTO.setTicketType(ticketDetailsDTO);

			VisitorsType visitorsType = ticketRate.getVisitorsType();
			VisitorsTypeDTO visitorsTypeDTO = new VisitorsTypeDTO();
			BeanUtils.copyProperties(visitorsType, visitorsTypeDTO);
			ticketRateDTO.setVisitorsType(visitorsTypeDTO);

			BeanUtils.copyProperties(ticketRate, ticketRateDTO);
			ticketRatesDTOs.add(ticketRateDTO);
		}
		return ticketRatesDTOs;
	}

	public List<TicketsRatesMasterDTO> getAllActiveTicketRates() {
		List<TicketsRatesMasterDTO> ticketRatesDTOs = new ArrayList<>();
		List<TicketsRatesMaster> ticketRates = ticketRateRepo.findAll();

		for (TicketsRatesMaster ticketRate: ticketRates) {
			TicketsRatesMasterDTO ticketRateDTO = new TicketsRatesMasterDTO();

			TicketDetails ticketDetails = ticketRate.getTicketType();
			TicketDetailsDTO ticketDetailsDTO = new TicketDetailsDTO();
			log.debug("TICKET {} " + ticketRate);
			BeanUtils.copyProperties(ticketDetails, ticketDetailsDTO);
			ticketRateDTO.setTicketType(ticketDetailsDTO);

			VisitorsType visitorsType = ticketRate.getVisitorsType();
			VisitorsTypeDTO visitorsTypeDTO = new VisitorsTypeDTO();
			BeanUtils.copyProperties(visitorsType, visitorsTypeDTO);
			ticketRateDTO.setVisitorsType(visitorsTypeDTO);

			BeanUtils.copyProperties(ticketRate, ticketRateDTO);
			ticketRatesDTOs.add(ticketRateDTO);
		}
		return ticketRatesDTOs;
	}

	@Transactional(value = TxType.REQUIRED)
	public void updateOrAddNewPrice(NewTicketRate newTicketRate, String username) {
		Integer count = ticketRateRepo.checkIfRateIsAvaiable(newTicketRate.getTicketId(), newTicketRate.getGroupId());
		log.debug("checkIfRateIsAvaiable " + count);
		RSCUser user = userDetailsService.getUserByUsername(username);
		if (count == 0) {
			addNewRate(newTicketRate, user);
		}
		else {
			updatePrice(newTicketRate, user);
		}
	}

	@Transactional(value = TxType.REQUIRED)
	public void addNewRate(NewTicketRate newTicketRate, RSCUser user) {
		TicketsRatesMaster ticketRate = new TicketsRatesMaster();
		Optional<TicketDetails> ticket = ticketDetailsRepo.findById(newTicketRate.getTicketId());
		Optional<VisitorsType> visitorType = visitorTypeService.getVisitorById(newTicketRate.getGroupId());
		ticketRate.setBillType(BillType.TICKET);
		ticketRate.setIsActive(true);
		ticketRate.setPrice(newTicketRate.getPrice());
		ticketRate.setRevisionNo(0);
		ticketRate.setRevisedAt(new Date());
		ticketRate.setUser(user);
		ticketRate.setTicketType(ticket.get());
		ticketRate.setVisitorsType(visitorType.get());
		ticketRateRepo.save(ticketRate);
	}

	@Transactional(value = TxType.REQUIRED)
	public void updatePrice(NewTicketRate newTicketRate, RSCUser user) {
		TicketsRatesMaster newRateMaster = new TicketsRatesMaster();
		TicketsRatesMaster rateMaster = ticketRateRepo.findByGroupAndTicketIds(newTicketRate.getTicketId(),
				newTicketRate.getGroupId());

		if (rateMaster.getIsActive() && rateMaster.getPrice() != newTicketRate.getPrice()) {
			rateMaster.setIsActive(false);
			rateMaster = ticketRateRepo.save(rateMaster);

			BeanUtils.copyProperties(rateMaster, newRateMaster);
			newRateMaster.setId(null);
			log.debug("Set new price: " + newTicketRate.getPrice() + " for Ticket ID: " + newTicketRate.getTicketId(),
					", Visitor ID: " + newTicketRate.getGroupId() + "and Group ID");
			newRateMaster.setPrice(newTicketRate.getPrice());
			newRateMaster.setIsActive(true);
			newRateMaster.setRevisedAt(java.util.Date.from(java.time.Instant.now()));
			newRateMaster.setRevisionNo(rateMaster.getRevisionNo() == null ? 1 : rateMaster.getRevisionNo() + 1);
			// log.debug(rateMaster.getUser().getName());
			newRateMaster.setUser(user);
			newRateMaster.setOldRateMaster(rateMaster);
			ticketRateRepo.save(newRateMaster);
		}
	}

	@Transactional(value = TxType.REQUIRED)
	public void updateRateForComboTicket(ArrayList<TicketDetailsDTO> tickets, String username) {
		tickets.forEach(ticket -> {
			// No Rate for Ticket
			TicketsRatesMasterDTO rateMaster = null;
			try {
				rateMaster = getTicketRateByGroup(ticket.getId(), ticket.getGroupId());
			}
			catch(Exception ex) {
				log.debug("Rate is null");
			}

			if (rateMaster == null) {
				if (ticket.getChecked() != null)
					addNewPrice(ticket, username);
			}
			else {
				log.debug("RATE MASTER IS AVAILABLE{}"+rateMaster);
				if (ticket.getChecked() != null) {
					if (!rateMaster.getPrice().equals(ticket.getPrice())) {
						log.debug("REPLACING RATE MASTER{}"+ticket);
						replacePrice(ticket, rateMaster.getId(), username);
					}
				}
				else {
					log.debug("REMOVING RATE MASTER{}"+ticket);
					removeTicketFromGroup(ticket, rateMaster.getId(), username);
				}
			}
		});
	}

	@Transactional(value = TxType.REQUIRED)
	public void removeTicketFromGroup(TicketDetailsDTO dto, Long rateMasterId, String username) {
		TicketsRatesMaster ticketsRatesMaster = ticketRateRepo.findById(rateMasterId)
				.orElseThrow(() -> new RuntimeException("Ticket not found"));
		ticketsRatesMaster.setIsActive(false);
		ticketsRatesMaster = ticketRateRepo.save(ticketsRatesMaster);
	}

	@Transactional(value = TxType.REQUIRED)
	public void replacePrice(TicketDetailsDTO dto, Long rateMasterId, String username) {
		TicketsRatesMaster ticketsRatesMaster = ticketRateRepo.findById(rateMasterId)
				.orElseThrow(() -> new RuntimeException("Ticket not found"));
		ticketsRatesMaster.setIsActive(false);
		ticketsRatesMaster = ticketRateRepo.save(ticketsRatesMaster);
		TicketsRatesMaster newRatesMaster = new TicketsRatesMaster();
		newRatesMaster.setRevisedAt(new Date());
		newRatesMaster.setRevisionNo(ticketsRatesMaster.getRevisionNo() + 1);
		newRatesMaster.setIsActive(true);
		newRatesMaster.setPrice(dto.getPrice());
		newRatesMaster.setOldRateMaster(ticketsRatesMaster);
		newRatesMaster.setBillType(BillType.TICKET);
		newRatesMaster.setTicketType(ticketDetailsService.getTicketsById(dto.getId()).get());
		newRatesMaster.setUser(userDetailsService.getUserByUsername(username));
		newRatesMaster.setVisitorsType(visitorTypeService.getVisitorById(dto.getGroupId()).get());
		ticketRateRepo.save(newRatesMaster);
	}

	@Transactional(value = TxType.REQUIRED)
	public void addNewPrice(TicketDetailsDTO ticket, String username) {
		TicketsRatesMaster newRatesMaster = new TicketsRatesMaster();
		newRatesMaster.setIsActive(true);
		newRatesMaster.setPrice(ticket.getPrice());
		newRatesMaster.setBillType(BillType.TICKET);
		newRatesMaster.setRevisedAt(new Date());
		newRatesMaster.setRevisionNo(0);
		newRatesMaster.setTicketType(ticketDetailsService.getTicketsById(ticket.getId()).get());
		newRatesMaster.setUser(userDetailsService.getUserByUsername(username));
		newRatesMaster.setVisitorsType(visitorTypeService.getVisitorById(ticket.getGroupId()).get());
		ticketRateRepo.save(newRatesMaster);
	}

	@Transactional(value = TxType.REQUIRED)
	public void updateRateForComboTicketBKP(TicketDetailsDTO ticket, String username) {

		TicketsRatesMasterDTO ticketsRatesMasterDTO = null;
		try {
			ticketsRatesMasterDTO = getTicketRateByGroup(ticket.getId(), ticket.getGroupId());
		}
		catch (NullPointerException ex) {
			log.debug(ex.getMessage());
		}
		catch (Exception ex) {
			log.debug(ex.getMessage());
		}

		if (ticketsRatesMasterDTO == null) {
			TicketsRatesMaster newRatesMaster = new TicketsRatesMaster();
			newRatesMaster.setIsActive(true);
			newRatesMaster.setPrice(ticket.getPrice());
			newRatesMaster.setBillType(BillType.TICKET);
			newRatesMaster.setRevisedAt(new Date());
			newRatesMaster.setRevisionNo(0);
			newRatesMaster.setTicketType(ticketDetailsService.getTicketsById(ticket.getId()).get());
			newRatesMaster.setUser(userDetailsService.getUserByUsername(username));
			newRatesMaster.setVisitorsType(visitorTypeService.getVisitorById(ticket.getGroupId()).get());

			ticketRateRepo.save(newRatesMaster);
		}
		else {
			TicketsRatesMaster ticketsRatesMaster = ticketRateRepo.findById(ticketsRatesMasterDTO.getId())
					.orElseThrow(() -> new RuntimeException("Ticket not found"));
			ticketsRatesMaster.setIsActive(false);
			ticketsRatesMaster = ticketRateRepo.save(ticketsRatesMaster);

			TicketsRatesMaster newRatesMaster = new TicketsRatesMaster();
			newRatesMaster.setRevisedAt(new Date());
			newRatesMaster.setRevisionNo(ticketsRatesMaster.getRevisionNo() + 1);
			newRatesMaster.setIsActive(true);
			newRatesMaster.setPrice(ticket.getPrice());
			newRatesMaster.setBillType(BillType.TICKET);
			// newRatesMaster.setRevisedAt(new Date());
			newRatesMaster.setTicketType(ticketDetailsService.getTicketsById(ticket.getId()).get());
			newRatesMaster.setUser(userDetailsService.getUserByUsername(username));
			newRatesMaster.setVisitorsType(visitorTypeService.getVisitorById(ticket.getGroupId()).get());
			ticketRateRepo.save(newRatesMaster);
		}
	}

	public List<TicketsRatesMasterDTO> getActiveParkingDetails() {
		List<TicketsRatesMasterDTO> ticketsRatesMasterDTOs = new java.util.ArrayList<TicketsRatesMasterDTO>();
		final List<TicketsRatesMaster> ticketsRatesMasters = ticketRateRepo.findActiveParking();
		for (TicketsRatesMaster ticketsRatesMaster: ticketsRatesMasters) {
			TicketsRatesMasterDTO ticketsRatesMasterDTO = new TicketsRatesMasterDTO();
			BeanUtils.copyProperties(ticketsRatesMaster, ticketsRatesMasterDTO);
			ParkingDetailsDTO parkingDetailsDTO = new ParkingDetailsDTO();
			BeanUtils.copyProperties(ticketsRatesMaster.getParkingDetails(), parkingDetailsDTO);
			ticketsRatesMasterDTO.setParkingDetails(parkingDetailsDTO);
			ticketsRatesMasterDTOs.add(ticketsRatesMasterDTO);
		}
		return ticketsRatesMasterDTOs;
	}

	@Transactional(value = TxType.REQUIRED)
	public void updateParkingRate(final ParkingPriceDTO parkingPriceDTO) {
		TicketsRatesMaster ticketsRatesMaster = ticketRateRepo.findByIsActiveAndParkingDetails_Id(true, parkingPriceDTO.getId());
		ticketsRatesMaster.setIsActive(false);
		ticketRateRepo.save(ticketsRatesMaster);
		TicketsRatesMaster newTicketsRatesMaster = new TicketsRatesMaster();
		BeanUtils.copyProperties(ticketsRatesMaster, newTicketsRatesMaster);
		newTicketsRatesMaster.setId(null);
		newTicketsRatesMaster.setOldRateMaster(ticketsRatesMaster);
		newTicketsRatesMaster.setRevisedAt(new Date());
		newTicketsRatesMaster.setRevisionNo(ticketsRatesMaster.getRevisionNo() + 1);
		newTicketsRatesMaster.setIsActive(true);
		newTicketsRatesMaster.setPrice(parkingPriceDTO.getPrice());
		ticketRateRepo.save(newTicketsRatesMaster);
	}

	List<TicketsRatesMaster> getRatesOfCombo(long comboId) {
		return ticketRateRepo.getAllActiveRatesOfGroup(comboId);
	}

	List<TicketsRatesMasterDTO> getAllActiveVisitorTicketsByComboId(long visitorsTypeId) {
		List<TicketsRatesMasterDTO> ticketsRatesMasterDTOs = new ArrayList<TicketsRatesMasterDTO>();
		final List<TicketsRatesMaster> ticketsRatesMasters = ticketRateRepo.getAllActiveRatesOfGroup(visitorsTypeId);
		ticketsRatesMasters.forEach(ticketsRatesMaster -> {
			TicketsRatesMasterDTO ticketsRatesMasterDTO = new TicketsRatesMasterDTO();
			BeanUtils.copyProperties(ticketsRatesMaster, ticketsRatesMasterDTO);
			TicketDetailsDTO ticketDetailsDTO = new TicketDetailsDTO();
			BeanUtils.copyProperties(ticketsRatesMaster.getTicketType(), ticketDetailsDTO);
			ticketsRatesMasterDTO.setTicketType(ticketDetailsDTO);
			VisitorsTypeDTO visitorsTypeDTO = new VisitorsTypeDTO();
			BeanUtils.copyProperties(ticketsRatesMaster.getVisitorsType(), visitorsTypeDTO);
			ticketsRatesMasterDTO.setVisitorsType(visitorsTypeDTO);
			// ParkingDetailsDTO parkingDetailsDTO = new ParkingDetailsDTO();
			// BeanUtils.copyProperties(ticketsRatesMasterDTO.getParkingDetailsDTO(), parkingDetailsDTO);
			// ticketsRatesMasterDTO.setParkingDetailsDTO(parkingDetailsDTO);
			ticketsRatesMasterDTOs.add(ticketsRatesMasterDTO);
		});
		return ticketsRatesMasterDTOs;
	}

	TicketsRatesMaster getActiveParkingRateFloat(long parkingId) {
		return ticketRateRepo.getActiveParkingRate(parkingId);
	}
}
