package com.rsc.bhopal.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rsc.bhopal.dtos.VisitorsTypeDTO;
import com.rsc.bhopal.dtos.report.RSCBReportTicket;
import com.rsc.bhopal.enums.GroupType;
import com.rsc.bhopal.service.TicketBillRowService;
import com.rsc.bhopal.service.VisitorTypeService;
import com.rsc.bhopal.service.report.RSCBReportSummary;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/report")
public class ReportController {
	@Autowired
	private VisitorTypeService visitorTypeService;

	@Autowired
	private TicketBillRowService ticketBillRowService;

	// private double grandTotal;

	@GetMapping(path = "/daily")
	public String getReport(Map<String, Object> attributes) {
		final List<String> singleVisitorsColumn = visitorTypeService.getAllVisitorTypes().stream().filter(visitorsTypeDTO -> visitorsTypeDTO.getGroupType() == GroupType.SINGLE).map(VisitorsTypeDTO::getName).collect(Collectors.toList());
		final List<String> comboVisitorsColumn = visitorTypeService.getAllVisitorTypes().stream().filter(visitorsTypeDTO -> visitorsTypeDTO.getGroupType() == GroupType.COMBO).map(VisitorsTypeDTO::getName).collect(Collectors.toList());
		attributes.put("singleVisitorsName", singleVisitorsColumn);
		attributes.put("comboVisitorsName", comboVisitorsColumn);

		attributes.put("startDateTime", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		attributes.put("endDateTime", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

		attributes.put("StartDate", "");
		attributes.put("EndDate", new SimpleDateFormat("dd MMM yy").format(new Date()));

		// grandTotal = 0;
		double []gross = {0d};
		final List<Long> visitorsSingleIds = visitorTypeService.getAllVisitorTypes().stream().filter(visitorsTypeDTO -> visitorsTypeDTO.getGroupType() == GroupType.SINGLE).map(VisitorsTypeDTO::getId).collect(Collectors.toList());
		final List<Long> visitorsComboIds = visitorTypeService.getAllVisitorTypes().stream().filter(visitorsTypeDTO -> visitorsTypeDTO.getGroupType() == GroupType.COMBO).map(VisitorsTypeDTO::getId).collect(Collectors.toList());
		// final LinkedHashMap<Long, Ticket> reportTable = arrangeDataInTable(visitorsId, ticketBillRowService.getTicketBillRows());
		// final List<Ticket> reportTable = arrangeDataInTable(visitorsId, ticketBillRowService.getTicketBillRows()).values().stream().collect(Collectors.toList());
		final LinkedHashMap<Long, RSCBReportTicket> singleTicketsReportTable = new LinkedHashMap<Long, RSCBReportTicket>(), comboTicketsReportTable = new LinkedHashMap<Long, RSCBReportTicket>();
		new RSCBReportSummary().arrangeDataInTable(visitorsSingleIds, visitorsComboIds, singleTicketsReportTable, comboTicketsReportTable, ticketBillRowService.getTicketBillRowsAtDateTime(new Date(), new Date()), gross);
		attributes.put("singleTicketsReportTable", singleTicketsReportTable.values().stream().collect(Collectors.toList()));
		attributes.put("comboTicketsReportTable", comboTicketsReportTable.values().stream().collect(Collectors.toList()));
		attributes.put("ticketSerials", ticketBillRowService.getTicketsSerialDec(new Date(), new Date()));
		attributes.put("grandTotal", gross[0]);

		return "reports/daily";
	}

	@PostMapping(path = "/daily")
	public String postReport(@RequestParam String startDateTime, @RequestParam String endDateTime, Map<String, Object> attributes) {
		final List<String> singleVisitorsColumn = visitorTypeService.getAllVisitorTypes().stream().filter(visitorsTypeDTO -> visitorsTypeDTO.getGroupType() == GroupType.SINGLE).map(VisitorsTypeDTO::getName).collect(Collectors.toList());
		final List<String> comboVisitorsColumn = visitorTypeService.getAllVisitorTypes().stream().filter(visitorsTypeDTO -> visitorsTypeDTO.getGroupType() == GroupType.COMBO).map(VisitorsTypeDTO::getName).collect(Collectors.toList());
		attributes.put("singleVisitorsName", singleVisitorsColumn);
		attributes.put("comboVisitorsName", comboVisitorsColumn);

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

		// grandTotal = 0;
		double []gross = {0d};
		final List<Long> visitorsSingleIds = visitorTypeService.getAllVisitorTypes().stream().filter(visitorsTypeDTO -> visitorsTypeDTO.getGroupType() == GroupType.SINGLE).map(VisitorsTypeDTO::getId).collect(Collectors.toList());
		final List<Long> visitorsComboIds = visitorTypeService.getAllVisitorTypes().stream().filter(visitorsTypeDTO -> visitorsTypeDTO.getGroupType() == GroupType.COMBO).map(VisitorsTypeDTO::getId).collect(Collectors.toList());
		// final LinkedHashMap<Long, Ticket> reportTable = arrangeDataInTable(visitorsId, ticketBillRowService.getTicketBillRowsAtDateTime(formattedStartDateTime, formattedEndDateTime));
		final LinkedHashMap<Long, RSCBReportTicket> singleTicketsReportTable = new LinkedHashMap<Long, RSCBReportTicket>(), comboTicketsReportTable = new LinkedHashMap<Long, RSCBReportTicket>();
		new RSCBReportSummary().arrangeDataInTable(visitorsSingleIds, visitorsComboIds, singleTicketsReportTable, comboTicketsReportTable, ticketBillRowService.getTicketBillRowsAtDateTime(formattedStartDateTime, formattedEndDateTime), gross);
		attributes.put("singleTicketsReportTable", singleTicketsReportTable.values().stream().collect(Collectors.toList()));
		attributes.put("comboTicketsReportTable", comboTicketsReportTable.values().stream().collect(Collectors.toList()));
		attributes.put("ticketSerials", ticketBillRowService.getTicketsSerialDec(formattedStartDateTime, formattedEndDateTime));
		attributes.put("grandTotal", gross[0]);

		return "reports/daily";
	}

	@GetMapping(path = "/summary")
	public String getReportSummary(Map<String, Object> attributes) {
		final List<String> singleVisitorsColumn = visitorTypeService.getAllVisitorTypes().stream().filter(visitorsTypeDTO -> visitorsTypeDTO.getGroupType() == GroupType.SINGLE).map(VisitorsTypeDTO::getName).collect(Collectors.toList());
		final List<String> comboVisitorsColumn = visitorTypeService.getAllVisitorTypes().stream().filter(visitorsTypeDTO -> visitorsTypeDTO.getGroupType() == GroupType.COMBO).map(VisitorsTypeDTO::getName).collect(Collectors.toList());
		attributes.put("singleVisitorsName", singleVisitorsColumn);
		attributes.put("comboVisitorsName", comboVisitorsColumn);

		attributes.put("startDateTime", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		attributes.put("endDateTime", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

		attributes.put("StartDate", "");
		attributes.put("EndDate", new SimpleDateFormat("dd MMM yy").format(new Date()));

		// grandTotal = 0;
		double []gross = {0d};

		final List<Long> visitorsSingleIds = visitorTypeService.getAllVisitorTypes().stream().filter(visitorsTypeDTO -> visitorsTypeDTO.getGroupType() == GroupType.SINGLE).map(VisitorsTypeDTO::getId).collect(Collectors.toList());
		final List<Long> visitorsComboIds = visitorTypeService.getAllVisitorTypes().stream().filter(visitorsTypeDTO -> visitorsTypeDTO.getGroupType() == GroupType.COMBO).map(VisitorsTypeDTO::getId).collect(Collectors.toList());
		final List<Long> visitorIds = new ArrayList<Long>() {{
			add(0l);
		}};
		// final LinkedHashMap<Long, Ticket> totalReportTickets = arrangeDataInTable(visitorsId, ticketBillRowService.getTicketBillRows());
		LinkedHashMap<Long, RSCBReportTicket[]> reportTableForSingleTickets = new RSCBReportSummary().arrangeTableForSingleTickets(visitorsSingleIds, ticketBillRowService.getOverallTicketSales(new Date(), new Date()), gross);
		LinkedHashMap<Long, RSCBReportTicket[]> reportTableForComboTickets = new RSCBReportSummary().arrangeTableForSingleTickets(visitorsComboIds, ticketBillRowService.getOverallTicketSales(visitorsComboIds.get(0) , "", new Date(), new Date()), gross);
		LinkedHashMap<Long, RSCBReportTicket[]> parkingTicketsReportTable = new RSCBReportSummary().arrangeTableForSingleTickets(visitorIds, ticketBillRowService.getOverallParkingTickets(0l, "", new Date(), new Date()), gross);
		attributes.put("singleTicketsReportTable", reportTableForSingleTickets);
		attributes.put("comboTicketsReportTable", reportTableForComboTickets);
		attributes.put("parkingTicketsReportTable", parkingTicketsReportTable);

		attributes.put("ticketSerials", ticketBillRowService.getTicketsSerialDec(new Date(), new Date()));
		attributes.put("grandTotal", gross[0]);

		return "reports/summary";
	}

	@PostMapping(path = "/summary")
	public String postReportSummary(@RequestParam String startDateTime, @RequestParam String endDateTime, Map<String, Object> attributes) {
		final List<String> singleVisitorsColumn = visitorTypeService.getAllVisitorTypes().stream().filter(visitorsTypeDTO -> visitorsTypeDTO.getGroupType() == GroupType.SINGLE).map(VisitorsTypeDTO::getName).collect(Collectors.toList());
		final List<String> comboVisitorsColumn = visitorTypeService.getAllVisitorTypes().stream().filter(visitorsTypeDTO -> visitorsTypeDTO.getGroupType() == GroupType.COMBO).map(VisitorsTypeDTO::getName).collect(Collectors.toList());
		attributes.put("singleVisitorsName", singleVisitorsColumn);
		attributes.put("comboVisitorsName", comboVisitorsColumn);

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

		// grandTotal = 0;
		double []gross = {0d};

		final List<Long> visitorsSingleIds = visitorTypeService.getAllVisitorTypes().stream().filter(visitorsTypeDTO -> visitorsTypeDTO.getGroupType() == GroupType.SINGLE).map(VisitorsTypeDTO::getId).collect(Collectors.toList());
		final List<Long> visitorsComboIds = visitorTypeService.getAllVisitorTypes().stream().filter(visitorsTypeDTO -> visitorsTypeDTO.getGroupType() == GroupType.COMBO).map(VisitorsTypeDTO::getId).collect(Collectors.toList());
		final List<Long> visitorIds = new ArrayList<Long>() {{
			add(0l);
		}};
		// final LinkedHashMap<Long, Ticket> totalReportTickets = arrangeDataInTable(visitorsId, ticketBillRowService.getTicketBillRows());
		LinkedHashMap<Long, RSCBReportTicket[]> reportTableForSingleTickets = new RSCBReportSummary().arrangeTableForSingleTickets(visitorsSingleIds, ticketBillRowService.getOverallTicketSales(formattedStartDateTime, formattedEndDateTime), gross);
		LinkedHashMap<Long, RSCBReportTicket[]> reportTableForComboTickets = new RSCBReportSummary().arrangeTableForComboTickets(visitorsComboIds, ticketBillRowService.getOverallTicketSales(visitorsComboIds.get(0), "", formattedStartDateTime, formattedEndDateTime), gross);
		LinkedHashMap<Long, RSCBReportTicket[]> parkingTicketsReportTable = new RSCBReportSummary().arrangeTableForSingleTickets(visitorIds, ticketBillRowService.getOverallParkingTickets(0l, "", formattedStartDateTime, formattedEndDateTime), gross);
		attributes.put("singleTicketsReportTable", reportTableForSingleTickets);
		attributes.put("comboTicketsReportTable", reportTableForComboTickets);
		attributes.put("parkingTicketsReportTable", parkingTicketsReportTable);

		attributes.put("ticketSerials", ticketBillRowService.getTicketsSerialDec(formattedStartDateTime, formattedEndDateTime));
		attributes.put("grandTotal", gross[0]);

		return "reports/summary";
	}
}
