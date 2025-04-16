package com.rsc.bhopal.controller;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rsc.bhopal.dtos.TicketBillRowDTO;
import com.rsc.bhopal.dtos.TicketReportTableDTO;
import com.rsc.bhopal.dtos.VisitorsTypeDTO;
import com.rsc.bhopal.enums.GroupType;
import com.rsc.bhopal.exception.TicketRateNotMaintainedException;
import com.rsc.bhopal.service.TicketBillRowService;
import com.rsc.bhopal.service.TicketsRatesService;
import com.rsc.bhopal.service.VisitorTypeService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
@RequestMapping("/report")
public class ReportController {
	@Autowired
	private TicketsRatesService ticketsRatesService;

	@Autowired
	private VisitorTypeService visitorTypeService;

	@Autowired
	private TicketBillRowService ticketBillRowService;

	private double grandTotal;

	@GetMapping(path = "/daily")
	public String getReport(Map<String, Object> attributes) {
		final List<String> visitorsColumn = visitorTypeService.getAllVisitorTypes().stream().filter(visitorsTypeDTO -> GroupType.SINGLE.equals(visitorsTypeDTO.getGroupType())).map(VisitorsTypeDTO::getName).collect(Collectors.toList());
		attributes.put("visitorsName", visitorsColumn);

		attributes.put("startDateTime", "");
		attributes.put("endDateTime", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

		attributes.put("StartDate", "");
		attributes.put("EndDate", new SimpleDateFormat("dd MMM yy").format(new Date()));

		grandTotal = 0;
		final List<Long> visitorsId = visitorTypeService.getAllVisitorTypes().stream().filter(visitorsTypeDTO -> GroupType.SINGLE.equals(visitorsTypeDTO.getGroupType())).map(VisitorsTypeDTO::getId).collect(Collectors.toList());
		// final LinkedHashMap<Long, Ticket> reportTable = arrangeDataInTable(visitorsId, ticketBillRowService.getTicketBillRows());
		final List<Ticket> reportTable = arrangeDataInTable(visitorsId, ticketBillRowService.getTicketBillRows()).values().stream().collect(Collectors.toList());
		attributes.put("reportTable", reportTable);
		attributes.put("ticketSerials", ticketBillRowService.getTicketsSerialDesc());
		attributes.put("grandTotal", grandTotal);

		return "reports/daily";
	}

	@PostMapping(path = "/daily")
	public String postReport(@RequestParam String startDateTime, @RequestParam String endDateTime, Map<String, Object> attributes) {
		final List<String> visitorsColumn = visitorTypeService.getAllVisitorTypes().stream().filter(visitorsTypeDTO -> GroupType.SINGLE.equals(visitorsTypeDTO.getGroupType())).map(VisitorsTypeDTO::getName).collect(Collectors.toList());
		attributes.put("visitorsName", visitorsColumn);

		attributes.put("startDateTime", startDateTime);
		attributes.put("endDateTime", endDateTime);

		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			attributes.put("StartDate", new SimpleDateFormat("dd MMM yy").format(simpleDateFormat.parse(startDateTime)));
			attributes.put("EndDate", new SimpleDateFormat("dd MMM yy").format(simpleDateFormat.parse(endDateTime)));
		}
		catch(ParseException ex) {
			log.debug("Exception in parsing Date Time");
		}

		/*
		Date formattedStarDateTime = null;
		Date formattedEndDateTime = null;
		try {
			formattedStarDateTime = new SimpleDateFormat("dd-MM-yyyy").parse(startDateTime);
			formattedEndDateTime = new SimpleDateFormat("dd-MM--yyyy").parse(endDateTime);
		}
		catch(ParseException ex) {
			log.debug("Exception in parsing Date Time");
		}
		*/

		/*
		LocalDateTime formattedStarDateTime = LocalDateTime.parse(startDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		LocalDateTime formattedEndDateTime = LocalDateTime.parse(endDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
		ticketBillRowService.getTicketBillRowsAtDateTime(formattedStarDateTime, formattedStarDateTime);
		*/

		Date formattedStartDateTime = null;
		Date formattedEndDateTime = null;
		try {
			formattedStartDateTime = new SimpleDateFormat("yyyy-MM-dd").parse(startDateTime);
			formattedEndDateTime = new SimpleDateFormat("yyyy-MM-dd").parse(endDateTime);
		}
		catch(ParseException ex) {
			log.debug("Exception in parsing Date Time");
		}

		grandTotal = 0;
		final List<Long> visitorsId = visitorTypeService.getAllVisitorTypes().stream().filter(visitorsTypeDTO -> GroupType.SINGLE.equals(visitorsTypeDTO.getGroupType())).map(VisitorsTypeDTO::getId).collect(Collectors.toList());
		// final LinkedHashMap<Long, Ticket> reportTable = arrangeDataInTable(visitorsId, ticketBillRowService.getTicketBillRowsAtDateTime(formattedStartDateTime, formattedEndDateTime));
		final List<Ticket> reportTable = arrangeDataInTable(visitorsId, ticketBillRowService.getTicketBillRowsAtDateTime(formattedStartDateTime, formattedEndDateTime)).values().stream().collect(Collectors.toList());
		attributes.put("reportTable", reportTable);
		attributes.put("ticketSerials", ticketBillRowService.getTicketsSerialAtDateTimeDesc(formattedStartDateTime, formattedEndDateTime));
		attributes.put("grandTotal", grandTotal);

		return "reports/daily";
	}

	/*
	@GetMapping(path = "/billrow")
	public @ResponseBody List<TicketBillRowDTO> getAllTicketsBillRow() {
		return ticketBillRowService.getAllTicketsBillRow();
	}
	*/

	/*
	@GetMapping(path = "/report")
	public @ResponseBody LinkedHashMap<Long, Ticket[]> getTicketsReportTable() {
		// return ticketBillRowService.getTicketsReportTable();
		final List<Long> visitorsId = visitorTypeService.getAllVisitorTypes().stream().filter(visitorsTypeDTO -> GroupType.SINGLE.equals(visitorsTypeDTO.getGroupType())).map(VisitorsTypeDTO::getId).collect(Collectors.toList());
		return arrangeTable(visitorsId);
	}
	*/

	@GetMapping(path = "/summary")
	public String getReportSummary(Map<String, Object> attributes) {
		final List<String> visitorsColumn = visitorTypeService.getAllVisitorTypes().stream().filter(visitorsTypeDTO -> GroupType.SINGLE.equals(visitorsTypeDTO.getGroupType())).map(VisitorsTypeDTO::getName).collect(Collectors.toList());
		attributes.put("visitorsName", visitorsColumn);

		attributes.put("startDateTime", "");
		attributes.put("endDateTime", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

		attributes.put("StartDate", "");
		attributes.put("EndDate", new SimpleDateFormat("dd MMM yy").format(new Date()));

		grandTotal = 0;

		final List<Long> visitorsId = visitorTypeService.getAllVisitorTypes().stream().filter(visitorsTypeDTO -> GroupType.SINGLE.equals(visitorsTypeDTO.getGroupType())).map(VisitorsTypeDTO::getId).collect(Collectors.toList());
		// final LinkedHashMap<Long, Ticket> totalReportTickets = arrangeDataInTable(visitorsId, ticketBillRowService.getTicketBillRows());
		LinkedHashMap<Long, Ticket[]> reportTables = arrangeTable(visitorsId, ticketBillRowService.getTicketsReportTable());
		attributes.put("reportTables", reportTables);

		attributes.put("ticketSerials", ticketBillRowService.getTicketsSerialDesc());
		attributes.put("grandTotal", grandTotal);

		return "reports/summary";
	}

	@PostMapping(path = "/summary")
	public String postReportSummary(@RequestParam String startDateTime, @RequestParam String endDateTime, Map<String, Object> attributes) {
		final List<String> visitorsColumn = visitorTypeService.getAllVisitorTypes().stream().filter(visitorsTypeDTO -> GroupType.SINGLE.equals(visitorsTypeDTO.getGroupType())).map(VisitorsTypeDTO::getName).collect(Collectors.toList());
		attributes.put("visitorsName", visitorsColumn);

		attributes.put("startDateTime", startDateTime);
		attributes.put("endDateTime", endDateTime);

		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		try {
			attributes.put("StartDate", new SimpleDateFormat("dd MMM yy").format(simpleDateFormat.parse(startDateTime)));
			attributes.put("EndDate", new SimpleDateFormat("dd MMM yy").format(simpleDateFormat.parse(endDateTime)));
		}
		catch(ParseException ex) {
			log.debug("Exception in parsing Date Time");
		}

		Date formattedStartDateTime = null;
		Date formattedEndDateTime = null;
		try {
			formattedStartDateTime = new SimpleDateFormat("yyyy-MM-dd").parse(startDateTime);
			formattedEndDateTime = new SimpleDateFormat("yyyy-MM-dd").parse(endDateTime);
		}
		catch(ParseException ex) {
			log.debug("Exception in parsing Date Time");
		}

		grandTotal = 0;

		final List<Long> visitorsId = visitorTypeService.getAllVisitorTypes().stream().filter(visitorsTypeDTO -> GroupType.SINGLE.equals(visitorsTypeDTO.getGroupType())).map(VisitorsTypeDTO::getId).collect(Collectors.toList());
		// final LinkedHashMap<Long, Ticket> totalReportTickets = arrangeDataInTable(visitorsId, ticketBillRowService.getTicketBillRows());
		LinkedHashMap<Long, Ticket[]> reportTables = arrangeTable(visitorsId, ticketBillRowService.getTicketsReportTableAtDateTime(formattedStartDateTime, formattedEndDateTime));
		attributes.put("reportTables", reportTables);

		attributes.put("ticketSerials", ticketBillRowService.getTicketsSerialAtDateTimeDesc(formattedStartDateTime, formattedEndDateTime));
		attributes.put("grandTotal", grandTotal);

		return "reports/summary";
	}

	private LinkedHashMap<Long, Ticket> arrangeDataInTable(List<Long> visitorIds, List<TicketBillRowDTO> ticketBillRowDTOs) {
		HashMap<Long, Integer> visitorIdToIndex = new HashMap<Long, Integer>();
		for (int index = 0; index < visitorIds.size(); ++index) {
			visitorIdToIndex.put(visitorIds.get(index), index);
		}
		// log.debug("Mapping: " + visitorIdToIndex);

		LinkedHashMap<Long, Ticket> tickets = new LinkedHashMap<Long, Ticket>();
		for (final TicketBillRowDTO ticketBillRowDTO: ticketBillRowDTOs) {
			// if (ticketBillRowDTO.getTicketsRatesMasterDTO().getBillType() != BillType.TICKET)
				// continue;

			Ticket ticket = tickets.get(ticketBillRowDTO.getTicketsRatesMasterDTO().getTicketType().getId());
			if (ticket == null) {
				ticket = new Ticket();
				ticket.setTicketId(ticketBillRowDTO.getTicketsRatesMasterDTO().getTicketType().getId());
				ticket.setTicketName(ticketBillRowDTO.getTicketsRatesMasterDTO().getTicketType().getName());
				ticket.setGroup(new HashMap<Long, Group>());

				Group group = new Group();
				group.setGroupId(ticketBillRowDTO.getTicketsRatesMasterDTO().getVisitorsType().getId());
				group.setGroupName(ticketBillRowDTO.getTicketsRatesMasterDTO().getVisitorsType().getName());
				group.setCount(ticketBillRowDTO.getTicketBillDTO().getPersons());
				group.setPrice(ticketBillRowDTO.getTicketsRatesMasterDTO().getPrice());
				group.setPersonCount(ticketBillRowDTO.getTicketBillDTO().getPersons());
				group.setTicketSerial(ticketBillRowDTO.getTicketBillDTO().getBillSummarize().getTicketSerial());
				ticket.getGroup().put(Long.valueOf(visitorIdToIndex.get(ticketBillRowDTO.getTicketsRatesMasterDTO().getVisitorsType().getId())), group);

				ticket.setSubTotal(ticketBillRowDTO.getTicketBillDTO().getPersons() * ticketBillRowDTO.getTicketsRatesMasterDTO().getPrice());

				tickets.put(ticketBillRowDTO.getTicketsRatesMasterDTO().getTicketType().getId(), ticket);

				grandTotal += ticketBillRowDTO.getTicketBillDTO().getPersons() * group.getPrice();
			}
			else {
				Group groups = ticket.getGroup().get(Long.valueOf(visitorIdToIndex.get(ticketBillRowDTO.getTicketsRatesMasterDTO().getVisitorsType().getId())));
				if (groups == null) {
					Group group = new Group();
					group.setGroupId(ticketBillRowDTO.getTicketsRatesMasterDTO().getVisitorsType().getId());
					group.setGroupName(ticketBillRowDTO.getTicketsRatesMasterDTO().getVisitorsType().getName());
					group.setCount(ticketBillRowDTO.getTicketBillDTO().getPersons());
					group.setPrice(ticketBillRowDTO.getTicketsRatesMasterDTO().getPrice());
					group.setPersonCount(ticketBillRowDTO.getTicketBillDTO().getPersons());
					group.setTicketSerial(ticketBillRowDTO.getTicketBillDTO().getBillSummarize().getTicketSerial());

					ticket.getGroup().put(Long.valueOf(visitorIdToIndex.get(ticketBillRowDTO.getTicketsRatesMasterDTO().getVisitorsType().getId())), group);
					ticket.setSubTotal(ticket.getSubTotal() + (ticketBillRowDTO.getTicketBillDTO().getPersons() * ticketBillRowDTO.getTicketsRatesMasterDTO().getPrice()));

					grandTotal += ticketBillRowDTO.getTicketBillDTO().getPersons() * group.getPrice();
				}
				else {
					groups.setCount(groups.getCount() + ticketBillRowDTO.getTicketBillDTO().getPersons());
					ticket.setSubTotal(ticket.getSubTotal() + (ticketBillRowDTO.getTicketBillDTO().getPersons() * ticketBillRowDTO.getTicketsRatesMasterDTO().getPrice()));

					grandTotal += ticketBillRowDTO.getTicketBillDTO().getPersons() * ticketBillRowDTO.getTicketsRatesMasterDTO().getPrice();
				}
			}
		}

		// Make object with zero values that were not present
		for (HashMap.Entry<Long, Ticket> ticket: tickets.entrySet()) {
			for (HashMap.Entry<Long, Integer> mappedIndex: visitorIdToIndex.entrySet()) {
				if (ticket.getValue().getGroup().get(Long.valueOf(mappedIndex.getValue())) == null) {
					Group group = new Group();
					group.setGroupName("");
					group.setCount(0);
					try {
						group.setPrice(ticketsRatesService.getTicketRateByGroup(ticket.getValue().getTicketId(), mappedIndex.getKey()).getPrice());
					}
					catch(TicketRateNotMaintainedException ex) {
						group.setPrice(0f);
					}
					group.setPersonCount(0);
					ticket.getValue().getGroup().put(Long.valueOf(mappedIndex.getValue()), group);
				}
			}
		}
		return tickets;
	}

	private LinkedHashMap<Long, Ticket[]> arrangeTable(List<Long> visitorIds, List<TicketReportTableDTO> ticketReportTableDTOs) {
		HashMap<Long, Integer> visitorIdToIndex = new HashMap<Long, Integer>();
		for (int index = 0; index < visitorIds.size(); ++index) {
			visitorIdToIndex.put(visitorIds.get(index), index);
		}

		// final short TOTAL_TICKET = 0;
		// final short ISSUED_TICKET = 1;
		// final short CANCELLED_TICKET = 2;
		final short CANCELLED_TICKET = 0;
		final short ISSUED_TICKET = 1;
		final short TOTAL_TICKET = 2;

		LinkedHashMap<Long, Ticket[]> row = new LinkedHashMap<Long, Ticket[]>();

		for (TicketReportTableDTO ticketReportTableDTO: ticketReportTableDTOs) {

			Ticket[] tickets = row.get(ticketReportTableDTO.getTicketId());
			if (tickets == null) {
				tickets = new Ticket[3];
				tickets[TOTAL_TICKET] = new Ticket();
				tickets[TOTAL_TICKET].setTicketId(ticketReportTableDTO.getTicketId());
				tickets[TOTAL_TICKET].setTicketName(ticketReportTableDTO.getTicketName());
				tickets[TOTAL_TICKET].setGroup(new HashMap<Long, Group>());

				Group group = new Group();
				group.setGroupId(ticketReportTableDTO.getVisitorId());
				group.setGroupName(ticketReportTableDTO.getVisitorName());
				group.setCount(ticketReportTableDTO.getPersons());
				group.setPrice(ticketReportTableDTO.getPrice());
				group.setPersonCount(ticketReportTableDTO.getPersons());
				group.setTicketSerial(ticketReportTableDTO.getTicketSerial());
				tickets[TOTAL_TICKET].getGroup().put(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())), group);

				tickets[TOTAL_TICKET].setSubTotal(ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice());

				row.put(ticketReportTableDTO.getTicketId(), tickets);

				grandTotal += ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice();

				if (ticketReportTableDTO.getCancelledStatus()) {
					// tickets[CANCELLED_TICKET] = tickets[TOTAL_TICKET];
					tickets[CANCELLED_TICKET] = new Ticket();
					tickets[CANCELLED_TICKET].setTicketId(ticketReportTableDTO.getTicketId());
					tickets[CANCELLED_TICKET].setTicketName(ticketReportTableDTO.getTicketName());
					tickets[CANCELLED_TICKET].setGroup(new HashMap<Long, Group>());

					Group cancelledTicketGroup = new Group();
					cancelledTicketGroup.setGroupId(ticketReportTableDTO.getVisitorId());
					cancelledTicketGroup.setGroupName(ticketReportTableDTO.getVisitorName());
					cancelledTicketGroup.setCount(ticketReportTableDTO.getPersons());
					cancelledTicketGroup.setPrice(ticketReportTableDTO.getPrice());
					cancelledTicketGroup.setPersonCount(ticketReportTableDTO.getPersons());
					cancelledTicketGroup.setTicketSerial(ticketReportTableDTO.getTicketSerial());

					tickets[CANCELLED_TICKET].getGroup().put(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())), cancelledTicketGroup);

					tickets[CANCELLED_TICKET].setSubTotal(ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice());

					tickets[ISSUED_TICKET] = null;
				}
				else {
					// tickets[ISSUED_TICKET] = tickets[TOTAL_TICKET];
					tickets[ISSUED_TICKET] = new Ticket();
					tickets[ISSUED_TICKET].setTicketId(ticketReportTableDTO.getTicketId());
					tickets[ISSUED_TICKET].setTicketName(ticketReportTableDTO.getTicketName());
					tickets[ISSUED_TICKET].setGroup(new HashMap<Long, Group>());

					Group issuedTicketGroup = new Group();
					issuedTicketGroup.setGroupId(ticketReportTableDTO.getVisitorId());
					issuedTicketGroup.setGroupName(ticketReportTableDTO.getVisitorName());
					issuedTicketGroup.setCount(ticketReportTableDTO.getPersons());
					issuedTicketGroup.setPrice(ticketReportTableDTO.getPrice());
					issuedTicketGroup.setPersonCount(ticketReportTableDTO.getPersons());
					issuedTicketGroup.setTicketSerial(ticketReportTableDTO.getTicketSerial());

					tickets[ISSUED_TICKET].getGroup().put(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())), issuedTicketGroup);

					tickets[ISSUED_TICKET].setSubTotal(ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice());

					tickets[CANCELLED_TICKET] = null;
				}
			}
			else {
				Group groups = tickets[TOTAL_TICKET].getGroup().get(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())));
				if (groups == null) {
					groups = new Group();
					groups.setGroupId(ticketReportTableDTO.getTicketId());
					groups.setGroupName(ticketReportTableDTO.getVisitorName());
					groups.setCount(ticketReportTableDTO.getPersons());
					groups.setPrice(ticketReportTableDTO.getPrice());
					groups.setPersonCount(ticketReportTableDTO.getPersons());
					groups.setTicketSerial(ticketReportTableDTO.getTicketSerial());

					tickets[TOTAL_TICKET].getGroup().put(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())), groups);
					tickets[TOTAL_TICKET].setSubTotal(tickets[TOTAL_TICKET].getSubTotal() + (ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice()));

					grandTotal += ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice();
				}
				else {
					groups.setCount(groups.getCount() + ticketReportTableDTO.getPersons());
					tickets[TOTAL_TICKET].setSubTotal(tickets[TOTAL_TICKET].getSubTotal() + (ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice()));

					grandTotal += ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice();
				}

				if (ticketReportTableDTO.getCancelledStatus()) {
					if (tickets[CANCELLED_TICKET] == null) {
						tickets[CANCELLED_TICKET] = new Ticket();
						tickets[CANCELLED_TICKET].setTicketId(ticketReportTableDTO.getTicketId());
						tickets[CANCELLED_TICKET].setTicketName(ticketReportTableDTO.getTicketName());
						tickets[CANCELLED_TICKET].setGroup(new HashMap<Long, Group>());

						Group group = new Group();
						group.setGroupId(ticketReportTableDTO.getVisitorId());
						group.setGroupName(ticketReportTableDTO.getVisitorName());
						group.setCount(ticketReportTableDTO.getPersons());
						group.setPrice(ticketReportTableDTO.getPrice());
						group.setPersonCount(ticketReportTableDTO.getPersons());
						group.setTicketSerial(ticketReportTableDTO.getTicketSerial());
						tickets[CANCELLED_TICKET].getGroup().put(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())), group);

						tickets[CANCELLED_TICKET].setSubTotal(ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice());

						// row.put(ticketReportTableDTO.getTicketId(), tickets);

						// grandTotal += ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice();
					}
					else {
						Group cancelledTicketGroup = tickets[CANCELLED_TICKET].getGroup().get(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())));
						if (cancelledTicketGroup == null) {
							cancelledTicketGroup = new Group();
							cancelledTicketGroup.setGroupId(ticketReportTableDTO.getTicketId());
							cancelledTicketGroup.setGroupName(ticketReportTableDTO.getVisitorName());
							cancelledTicketGroup.setCount(ticketReportTableDTO.getPersons());
							cancelledTicketGroup.setPrice(ticketReportTableDTO.getPrice());
							cancelledTicketGroup.setPersonCount(ticketReportTableDTO.getPersons());
							cancelledTicketGroup.setTicketSerial(ticketReportTableDTO.getTicketSerial());
		
							tickets[CANCELLED_TICKET].getGroup().put(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())), cancelledTicketGroup);
						}
						else {
							cancelledTicketGroup.setCount(cancelledTicketGroup.getCount() + ticketReportTableDTO.getPersons());
							tickets[CANCELLED_TICKET].setSubTotal(tickets[CANCELLED_TICKET].getSubTotal() + (ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice()));
						}
					}
				}
				else {
					if (tickets[ISSUED_TICKET] == null) {
						tickets[ISSUED_TICKET] = new Ticket();
						tickets[ISSUED_TICKET].setTicketId(ticketReportTableDTO.getTicketId());
						tickets[ISSUED_TICKET].setTicketName(ticketReportTableDTO.getTicketName());
						tickets[ISSUED_TICKET].setGroup(new HashMap<Long, Group>());

						Group group = new Group();
						group.setGroupId(ticketReportTableDTO.getVisitorId());
						group.setGroupName(ticketReportTableDTO.getVisitorName());
						group.setCount(ticketReportTableDTO.getPersons());
						group.setPrice(ticketReportTableDTO.getPrice());
						group.setPersonCount(ticketReportTableDTO.getPersons());
						group.setTicketSerial(ticketReportTableDTO.getTicketSerial());
						tickets[ISSUED_TICKET].getGroup().put(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())), group);

						tickets[ISSUED_TICKET].setSubTotal(ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice());

						// row.put(ticketReportTableDTO.getTicketId(), tickets);

						// grandTotal += ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice();
					}
					else {
						Group issuededTicketGroup = tickets[ISSUED_TICKET].getGroup().get(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())));
						if (issuededTicketGroup == null) {
							issuededTicketGroup = new Group();
							issuededTicketGroup.setGroupId(ticketReportTableDTO.getTicketId());
							issuededTicketGroup.setGroupName(ticketReportTableDTO.getVisitorName());
							issuededTicketGroup.setCount(ticketReportTableDTO.getPersons());
							issuededTicketGroup.setPrice(ticketReportTableDTO.getPrice());
							issuededTicketGroup.setPersonCount(ticketReportTableDTO.getPersons());
							issuededTicketGroup.setTicketSerial(ticketReportTableDTO.getTicketSerial());
		
							tickets[ISSUED_TICKET].getGroup().put(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())), issuededTicketGroup);
						}
						else {
							issuededTicketGroup.setCount(issuededTicketGroup.getCount() + ticketReportTableDTO.getPersons());
							tickets[ISSUED_TICKET].setSubTotal(tickets[ISSUED_TICKET].getSubTotal() + (ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice()));
						}
					}
				}
			}
		}

		// Make object with zero values that were not present
		for (HashMap.Entry<Long, Ticket[]> element: row.entrySet()) {
			for (short index = 0; index < element.getValue().length; ++index) {
				for (HashMap.Entry<Long, Integer> mappedIndex: visitorIdToIndex.entrySet()) {
					if (element.getValue()[index] == null) {
						Ticket ticket = new Ticket();
						ticket.setTicketId(0L);
						ticket.setTicketName("");
						ticket.setSubTotal(0);
						ticket.setGroup(new HashMap<Long, Group>());
						element.getValue()[index] = ticket;
					}
					if (element.getValue()[index].getGroup().get(Long.valueOf(mappedIndex.getValue())) == null) {
						Group group = new Group();
						group.setGroupName("");
						group.setCount(0);
						try {
							group.setPrice(ticketsRatesService.getTicketRateByGroup(element.getValue()[index].getTicketId(), mappedIndex.getKey()).getPrice());
						}
						catch(TicketRateNotMaintainedException ex) {
							group.setPrice(0f);
						}
						catch(java.util.NoSuchElementException ex) {
							group.setPrice(0f);
						}
						group.setPersonCount(0);
						element.getValue()[index].getGroup().put(Long.valueOf(mappedIndex.getValue()), group);
					}
				}
			}
		}
		return row;
	}

	@Data
	private class Ticket {
		private Long ticketId;
		private String ticketName;

		private HashMap<Long, Group> group;
		private double subTotal;
	}

	@Data
	private class Group {
		private Long groupId;
		private String groupName;
		private Integer personCount;
		private Integer count;
		private Float price;
		private BigInteger ticketSerial;
	}
}
