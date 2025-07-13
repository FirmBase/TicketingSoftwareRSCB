package com.rsc.bhopal.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.rsc.bhopal.service.TicketBillRowServiceOld;
import com.rsc.bhopal.service.VisitorTypeService;
import com.rsc.bhopal.service.report.RSCBReportSummaryOld;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/report-old")
public class ReportControllerOld {
	@Autowired
	private VisitorTypeService visitorTypeService;

	@Autowired
	private TicketBillRowServiceOld ticketBillRowService;

	// private double grandTotal;

	@GetMapping(path = "/daily")
	public String getReport(Map<String, Object> attributes) {
		final List<String> visitorsColumn = visitorTypeService.getAllVisitorTypes().stream().map(VisitorsTypeDTO::getName).collect(Collectors.toList());
		attributes.put("visitorsName", visitorsColumn);

		attributes.put("startDateTime", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		attributes.put("endDateTime", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

		attributes.put("StartDate", "");
		attributes.put("EndDate", new SimpleDateFormat("dd MMM yy").format(new Date()));

		// grandTotal = 0;
		double []gross = {0d};
		final List<Long> visitorsId = visitorTypeService.getAllVisitorTypes().stream().map(VisitorsTypeDTO::getId).collect(Collectors.toList());
		// final LinkedHashMap<Long, Ticket> reportTable = arrangeDataInTable(visitorsId, ticketBillRowService.getTicketBillRows());
		// final List<Ticket> reportTable = arrangeDataInTable(visitorsId, ticketBillRowService.getTicketBillRows()).values().stream().collect(Collectors.toList());
		List<RSCBReportTicket> reportTable = new RSCBReportSummaryOld().arrangeDataInTable(visitorsId, ticketBillRowService.getTicketBillRowsAtDateTime(new Date(), new Date()), gross).values().stream().collect(Collectors.toList());
		attributes.put("reportTable", reportTable);
		attributes.put("ticketSerials", ticketBillRowService.getTicketsSerialDec(new Date(), new Date()));
		attributes.put("grandTotal", gross[0]);

		return "reports/daily_old";
	}

	@PostMapping(path = "/daily")
	public String postReport(@RequestParam String startDateTime, @RequestParam String endDateTime, Map<String, Object> attributes) {
		final List<String> visitorsColumn = visitorTypeService.getAllVisitorTypes().stream().map(VisitorsTypeDTO::getName).collect(Collectors.toList());
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

		// grandTotal = 0;
		double []gross = {0d};
		final List<Long> visitorsId = visitorTypeService.getAllVisitorTypes().stream().map(VisitorsTypeDTO::getId).collect(Collectors.toList());
		// final LinkedHashMap<Long, Ticket> reportTable = arrangeDataInTable(visitorsId, ticketBillRowService.getTicketBillRowsAtDateTime(formattedStartDateTime, formattedEndDateTime));
		final List<RSCBReportTicket> reportTable = new RSCBReportSummaryOld().arrangeDataInTable(visitorsId, ticketBillRowService.getTicketBillRowsAtDateTime(formattedStartDateTime, formattedEndDateTime), gross).values().stream().collect(Collectors.toList());
		attributes.put("reportTable", reportTable);
		attributes.put("ticketSerials", ticketBillRowService.getTicketsSerialDec(formattedStartDateTime, formattedEndDateTime));
		attributes.put("grandTotal", gross[0]);

		return "reports/daily_old";
	}

	@GetMapping(path = "/summary")
	public String getReportSummary(Map<String, Object> attributes) {
		final List<String> visitorsColumn = visitorTypeService.getAllVisitorTypes().stream().map(VisitorsTypeDTO::getName).collect(Collectors.toList());
		attributes.put("visitorsName", visitorsColumn);

		attributes.put("startDateTime", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
		attributes.put("endDateTime", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

		attributes.put("StartDate", "");
		attributes.put("EndDate", new SimpleDateFormat("dd MMM yy").format(new Date()));

		// grandTotal = 0;
		double []gross = {0d};

		final List<Long> visitorsId = visitorTypeService.getAllVisitorTypes().stream().map(VisitorsTypeDTO::getId).collect(Collectors.toList());
		// final LinkedHashMap<Long, Ticket> totalReportTickets = arrangeDataInTable(visitorsId, ticketBillRowService.getTicketBillRows());
		LinkedHashMap<Long, RSCBReportTicket[]> reportTables = new RSCBReportSummaryOld().arrangeTable(visitorsId, ticketBillRowService.getOverallTicketSales(new Date(), new Date()), gross);
		attributes.put("reportTables", reportTables);

		attributes.put("ticketSerials", ticketBillRowService.getTicketsSerialDec(new Date(), new Date()));
		attributes.put("grandTotal", gross[0]);

		return "reports/summary_old";
	}

	@PostMapping(path = "/summary")
	public String postReportSummary(@RequestParam String startDateTime, @RequestParam String endDateTime, Map<String, Object> attributes) {
		final List<String> visitorsColumn = visitorTypeService.getAllVisitorTypes().stream().map(VisitorsTypeDTO::getName).collect(Collectors.toList());
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

		// grandTotal = 0;
		double []gross = {0d};

		final List<Long> visitorsId = visitorTypeService.getAllVisitorTypes().stream().map(VisitorsTypeDTO::getId).collect(Collectors.toList());
		// final LinkedHashMap<Long, Ticket> totalReportTickets = arrangeDataInTable(visitorsId, ticketBillRowService.getTicketBillRows());
		LinkedHashMap<Long, RSCBReportTicket[]> reportTables = new RSCBReportSummaryOld().arrangeTable(visitorsId, ticketBillRowService.getOverallTicketSales(formattedStartDateTime, formattedEndDateTime), gross);
		attributes.put("reportTables", reportTables);

		attributes.put("ticketSerials", ticketBillRowService.getTicketsSerialDec(formattedStartDateTime, formattedEndDateTime));
		attributes.put("grandTotal", gross[0]);

		return "reports/summary_old";
	}
}
