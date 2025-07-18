package com.rsc.bhopal.service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rsc.bhopal.dtos.BillSummarize;
import com.rsc.bhopal.dtos.RSCUserDTO;
import com.rsc.bhopal.dtos.TicketBillDTO;
import com.rsc.bhopal.dtos.TicketBillRowDTO;
import com.rsc.bhopal.dtos.TicketDetailsDTO;
import com.rsc.bhopal.dtos.TicketReportTableDTO;
import com.rsc.bhopal.dtos.TicketsRatesMasterDTO;
import com.rsc.bhopal.dtos.VisitorsTypeDTO;
import com.rsc.bhopal.entity.TicketBillRow;
import com.rsc.bhopal.projections.TicketDailyReport;
import com.rsc.bhopal.repos.TicketBillRowRepository;
import com.rsc.bhopal.utills.CommonUtills;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class TicketBillRowService {
	@Autowired
	private TicketBillRowRepository ticketBillRowRepository;

	public List<TicketBillRowDTO> getTicketBillRows() {
		return ticketBillRowToTicketBillRowDTOs(ticketBillRowRepository.getTicketBillRows());
	}

	public List<TicketBillRowDTO> getTicketBillRowsAtDateTime(Date startDateTime, Date endDateTime) {
		Timestamp timestampStartDateTime = new Timestamp(startDateTime.getTime());
		Timestamp timestampEndDateTime = new Timestamp(endDateTime.getTime());
		return ticketBillRowToTicketBillRowDTOs(ticketBillRowRepository.getTicketBillRowsAtDateTime(timestampStartDateTime, timestampEndDateTime));
	}

	public List<TicketBillRowDTO> getAllTicketsBillRow() {
		return ticketBillRowToTicketBillRowDTOs(ticketBillRowRepository.findAll());
	}

	public List<BigInteger> getTicketsSerialDec() {
		return ticketBillRowRepository.getTicketsSerialDec();
	}

	public List<BigInteger> getTicketsSerialDec(Date startDateTime, Date endDateTime) {
		Timestamp timestampStartDateTime = new Timestamp(startDateTime.getTime());
		Timestamp timestampEndDateTime = new Timestamp(endDateTime.getTime());
		return ticketBillRowRepository.getTicketsSerialDec(timestampStartDateTime, timestampEndDateTime);
	}

	public List<TicketBillRowDTO> getCancelledStatusDesc(boolean cancelledStatus) {
		return ticketBillRowToTicketBillRowDTOs(ticketBillRowRepository.getCancelledStatusDec(cancelledStatus));
	}

	public List<TicketBillRowDTO> getCancelledStatusAtDateTimeDesc(Date startDateTime, Date endDateTime, boolean cancelledStatus) {
		Timestamp timestampStartDateTime = new Timestamp(startDateTime.getTime());
		Timestamp timestampEndDateTime = new Timestamp(endDateTime.getTime());
		return ticketBillRowToTicketBillRowDTOs(ticketBillRowRepository.getCancelledStatusDec(timestampStartDateTime, timestampEndDateTime, cancelledStatus));
	}

	// Single Type Ticket
	public List<TicketReportTableDTO> getOverallTicketSales() {
		List<TicketReportTableDTO> ticketReportTableDTOs = new ArrayList<TicketReportTableDTO>();
		ticketBillRowRepository.getOverallTicketSales().forEach(ticketReportTable -> {
			TicketReportTableDTO ticketReportTableDTO = new TicketReportTableDTO();
			BeanUtils.copyProperties(ticketReportTable, ticketReportTableDTO);
			ticketReportTableDTOs.add(ticketReportTableDTO);
		});
		return ticketReportTableDTOs;
	}

	// Combo Type Ticket
	public List<TicketReportTableDTO> getOverallTicketSales(long visitorId, String visitorName) {
		List<TicketReportTableDTO> ticketReportTableDTOs = new ArrayList<TicketReportTableDTO>();
		ticketBillRowRepository.getOverallTicketSales(visitorId, visitorName).forEach(ticketReportTable -> {
			TicketReportTableDTO ticketReportTableDTO = new TicketReportTableDTO();
			BeanUtils.copyProperties(ticketReportTable, ticketReportTableDTO);
			ticketReportTableDTOs.add(ticketReportTableDTO);
		});
		return ticketReportTableDTOs;
	}

	// Single Type Ticket
	public List<TicketReportTableDTO> getOverallTicketSales(Date startDateTime, Date endDateTime) {
		Timestamp timestampStartDateTime = new Timestamp(startDateTime.getTime());
		Timestamp timestampEndDateTime = new Timestamp(endDateTime.getTime());

		List<TicketReportTableDTO> ticketReportTableDTOs = new ArrayList<TicketReportTableDTO>();
		ticketBillRowRepository.getOverallTicketSales(timestampStartDateTime, timestampEndDateTime).forEach(ticketReportTable -> {
			TicketReportTableDTO ticketReportTableDTO = new TicketReportTableDTO();
			BeanUtils.copyProperties(ticketReportTable, ticketReportTableDTO);
			ticketReportTableDTOs.add(ticketReportTableDTO);
		});
		return ticketReportTableDTOs;
	}

	// Combo Type Ticket
	public List<TicketReportTableDTO> getOverallTicketSales(long visitorId, String visitorName, Date startDateTime, Date endDateTime) {
		Timestamp timestampStartDateTime = new Timestamp(startDateTime.getTime());
		Timestamp timestampEndDateTime = new Timestamp(endDateTime.getTime());

		List<TicketReportTableDTO> ticketReportTableDTOs = new ArrayList<TicketReportTableDTO>();
		ticketBillRowRepository.getOverallTicketSales(visitorId, visitorName, timestampStartDateTime, timestampEndDateTime).forEach(ticketReportTable -> {
			TicketReportTableDTO ticketReportTableDTO = new TicketReportTableDTO();
			BeanUtils.copyProperties(ticketReportTable, ticketReportTableDTO);
			ticketReportTableDTOs.add(ticketReportTableDTO);
		});
		return ticketReportTableDTOs;
	}

	// Parking Ticket
	public List<TicketReportTableDTO> getOverallParkingTickets(long visitorId, String visitorName) {
		List<TicketReportTableDTO> ticketReportTableDTOs = new ArrayList<TicketReportTableDTO>();
		ticketBillRowRepository.getOverallParkingTickets(visitorId, visitorName).forEach(ticketReportTable -> {
			TicketReportTableDTO ticketReportTableDTO = new TicketReportTableDTO();
			BeanUtils.copyProperties(ticketReportTable, ticketReportTableDTO);
			ticketReportTableDTOs.add(ticketReportTableDTO);
		});
		return ticketReportTableDTOs;
	}

	// Parking Ticket
	public List<TicketReportTableDTO> getOverallParkingTickets(long visitorId, String visitorName, Date startDateTime, Date endDateTime) {
		Timestamp timestampStartDateTime = new Timestamp(startDateTime.getTime());
		Timestamp timestampEndDateTime = new Timestamp(endDateTime.getTime());

		List<TicketReportTableDTO> ticketReportTableDTOs = new ArrayList<TicketReportTableDTO>();
		ticketBillRowRepository.getOverallParkingTickets(visitorId, visitorName, timestampStartDateTime, timestampEndDateTime).forEach(ticketReportTable -> {
			TicketReportTableDTO ticketReportTableDTO = new TicketReportTableDTO();
			BeanUtils.copyProperties(ticketReportTable, ticketReportTableDTO);
			ticketReportTableDTOs.add(ticketReportTableDTO);
		});
		return ticketReportTableDTOs;
	}

	public List<TicketDailyReport> getDetailedReport(Short year) {
		return ticketBillRowRepository.getDetailedReport(year);
	}

	public List<TicketDailyReport> getDetailedReport(Date startDateTime, Date endStartTime) {
		return ticketBillRowRepository.getDetailedReport(new java.sql.Date(startDateTime.getTime()), new java.sql.Date(endStartTime.getTime()));
	}

	private List<TicketBillRowDTO> ticketBillRowToTicketBillRowDTOs(List<TicketBillRow> ticketBillRows) {
		List<TicketBillRowDTO> ticketBillRowDTOs = new ArrayList<TicketBillRowDTO>();
		ticketBillRows.forEach(ticketBillRow -> {
			TicketBillRowDTO ticketBillRowDTO = new TicketBillRowDTO();
			BeanUtils.copyProperties(ticketBillRow, ticketBillRowDTO);

			TicketBillDTO ticketBillDTO = new TicketBillDTO();
			BeanUtils.copyProperties(ticketBillRow.getGeneratedTicket(), ticketBillDTO);
			ticketBillRowDTO.setTicketBillDTO(ticketBillDTO);

			RSCUserDTO rscUserDTO = new RSCUserDTO();
			BeanUtils.copyProperties(ticketBillRow.getGeneratedTicket().getGeneratedBy(), rscUserDTO);
			ticketBillRowDTO.getTicketBillDTO().setGeneratedBy(rscUserDTO.getUsername());

			BillSummarize billSummarize = CommonUtills.convertJSONToObject(ticketBillDTO.getTicketPayload(), BillSummarize.class);
			ticketBillDTO.setBillSummarize(billSummarize);

			TicketsRatesMasterDTO ticketsRatesMasterDTO = new TicketsRatesMasterDTO();
			BeanUtils.copyProperties(ticketBillRow.getRate(), ticketsRatesMasterDTO);
			ticketBillRowDTO.setTicketsRatesMasterDTO(ticketsRatesMasterDTO);

			if (ticketBillRow.getRate().getTicketType() != null) {
				TicketDetailsDTO ticketDetailsDTO = new TicketDetailsDTO();
				BeanUtils.copyProperties(ticketBillRow.getRate().getTicketType(), ticketDetailsDTO);
				ticketBillRowDTO.getTicketsRatesMasterDTO().setTicketType(ticketDetailsDTO);
			}

			if (ticketBillRow.getRate().getVisitorsType() != null) {
				VisitorsTypeDTO visitorsTypeDTO = new VisitorsTypeDTO();
				BeanUtils.copyProperties(ticketBillRow.getRate().getVisitorsType(), visitorsTypeDTO);
				ticketBillRowDTO.getTicketsRatesMasterDTO().setVisitorsType(visitorsTypeDTO);
			}

			ticketBillRowDTOs.add(ticketBillRowDTO);
		});
		return ticketBillRowDTOs;
	}
}
