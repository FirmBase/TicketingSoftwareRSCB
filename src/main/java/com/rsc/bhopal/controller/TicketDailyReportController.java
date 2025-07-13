package com.rsc.bhopal.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rsc.bhopal.dtos.ParkingDetailsDTO;
import com.rsc.bhopal.dtos.TicketDetailsDTO;
import com.rsc.bhopal.dtos.VisitorsTypeDTO;
import com.rsc.bhopal.enums.GroupType;
import com.rsc.bhopal.projections.TicketDailyReport;
import com.rsc.bhopal.service.ParkingService;
import com.rsc.bhopal.service.TicketBillRowService;
import com.rsc.bhopal.service.TicketDetailsService;
import com.rsc.bhopal.service.VisitorTypeService;
import com.rsc.bhopal.service.report.DetailedReport;
import com.rsc.bhopal.utills.DailyReportExcel;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/report")
public class TicketDailyReportController {
	@Autowired
	private TicketBillRowService ticketBillRowService;

	@Autowired
	private TicketDetailsService ticketDetailsService;

	@Autowired
	private VisitorTypeService visitorTypeService;

	@Autowired
	private ParkingService parkingService;

	@GetMapping(path = "/detailed-report")
	public String getRequestReport() {
		return "redirect:detailed-report/" + new SimpleDateFormat("yyyy").format(new Date());
	}

	@GetMapping(path = "/detailed-report/{year}")
	public String getRequestReport(@PathVariable Short year, Model attributes) {
		attributes.addAttribute("startDateTime", LocalDate.now().with(TemporalAdjusters.firstDayOfYear()));
		attributes.addAttribute("endDateTime", LocalDate.now().with(TemporalAdjusters.lastDayOfYear()));

		final Map<Long, String> ticketsMap = ticketDetailsService.getAllTickets().stream().collect(Collectors.toMap(TicketDetailsDTO::getId, TicketDetailsDTO::getName));
		final List<VisitorsTypeDTO> vistitorsList = visitorTypeService.getAllActiveVisitorTypes();
		final Map<Long, String> visitorsSingleMap = vistitorsList.stream().filter(visitorsTypeDTO -> GroupType.SINGLE.equals(visitorsTypeDTO.getGroupType())).collect(Collectors.toMap(VisitorsTypeDTO::getId, VisitorsTypeDTO::getName));
		final Map<Long, String> visitorsComboMap = vistitorsList.stream().filter(visitorsTypeDTO -> GroupType.COMBO.equals(visitorsTypeDTO.getGroupType())).collect(Collectors.toMap(VisitorsTypeDTO::getId, VisitorsTypeDTO::getName));
		final Map<Long, String> parkingsMap = parkingService.getParkingDetails().stream().collect(Collectors.toMap(ParkingDetailsDTO::getId, ParkingDetailsDTO::getName));
		// final Map<Long, String> visitorsMap = visitorTypeService.getAllVisitorTypes().stream().collect(Collectors.toMap(VisitorsTypeDTO::getId, VisitorsTypeDTO::getName));

		attributes.addAttribute("ticketsName", ticketsMap.values().stream().collect(Collectors.toList()));
		attributes.addAttribute("visitorsSingleName", visitorsSingleMap.values().stream().collect(Collectors.toList()));
		attributes.addAttribute("visitorsComboName", visitorsComboMap.values().stream().collect(Collectors.toList()));
		attributes.addAttribute("parkingsName", parkingsMap.values().stream().collect(Collectors.toList()));
		double []grandTotal = new double[1];

		final List<TicketDailyReport> ticketDailyReports = ticketBillRowService.getDetailedReport(year);
		attributes.addAttribute("bills", new DetailedReport().arrange(ticketDailyReports, ticketsMap, visitorsSingleMap, visitorsComboMap, parkingsMap, grandTotal));
		attributes.addAttribute("ticketSerials", new int[2]);
		attributes.addAttribute("grandTotal", grandTotal[0]);
		return "reports/detailed_report";
	}

	@PostMapping(path = "/detailed-report")
	public String postRequestReport(@RequestParam String startDateTime, @RequestParam String endDateTime, Model attributes) {
		if (!(startDateTime.isEmpty() || endDateTime.isEmpty())) {
			final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			try {
				attributes.addAttribute("startDateTime", startDateTime);
				attributes.addAttribute("endDateTime", endDateTime);

				final Map<Long, String> ticketsMap = ticketDetailsService.getAllTickets().stream().collect(Collectors.toMap(TicketDetailsDTO::getId, TicketDetailsDTO::getName));
				final List<VisitorsTypeDTO> vistitorsList = visitorTypeService.getAllActiveVisitorTypes();
				final Map<Long, String> visitorsSingleMap = vistitorsList.stream().filter(visitorsTypeDTO -> GroupType.SINGLE.equals(visitorsTypeDTO.getGroupType())).collect(Collectors.toMap(VisitorsTypeDTO::getId, VisitorsTypeDTO::getName));
				final Map<Long, String> visitorsComboMap = vistitorsList.stream().filter(visitorsTypeDTO -> GroupType.COMBO.equals(visitorsTypeDTO.getGroupType())).collect(Collectors.toMap(VisitorsTypeDTO::getId, VisitorsTypeDTO::getName));
				final Map<Long, String> parkingsMap = parkingService.getParkingDetails().stream().collect(Collectors.toMap(ParkingDetailsDTO::getId, ParkingDetailsDTO::getName));
				// final Map<Long, String> visitorsMap = visitorTypeService.getAllVisitorTypes().stream().collect(Collectors.toMap(VisitorsTypeDTO::getId, VisitorsTypeDTO::getName));

				attributes.addAttribute("ticketsName", ticketsMap.values().stream().collect(Collectors.toList()));
				attributes.addAttribute("visitorsSingleName", visitorsSingleMap.values().stream().collect(Collectors.toList()));
				attributes.addAttribute("visitorsComboName", visitorsComboMap.values().stream().collect(Collectors.toList()));
				attributes.addAttribute("parkingsName", parkingsMap.values().stream().collect(Collectors.toList()));
				double []grandTotal = new double[1];

				final List<TicketDailyReport> ticketDailyReports = ticketBillRowService.getDetailedReport(simpleDateFormat.parse(startDateTime), simpleDateFormat.parse(endDateTime));
				attributes.addAttribute("bills", new DetailedReport().arrange(ticketDailyReports, ticketsMap, visitorsSingleMap, visitorsComboMap, parkingsMap, grandTotal));
				attributes.addAttribute("ticketSerials", new int[2]);
				attributes.addAttribute("grandTotal", grandTotal[0]);
			}
			catch (ParseException parseException) {
				parseException.printStackTrace();
				return "redirect:detailed-report";
			}
		}
		return "reports/detailed_report";
	}

	@GetMapping(path = "/detailed-report/xlsx")
	public String getRequestReportXLSX() {
		return "redirect:xlsx/" + new SimpleDateFormat("yyyy").format(new Date());
	}

	@GetMapping(value = "/detailed-report/xlsx/{year}", produces = MediaType.APPLICATION_PDF_VALUE)
	public void postRequestReportXLSX(@PathVariable Short year, HttpServletResponse httpServletResponse) throws IOException {
		// httpServletResponse.setContentType("application/xlsx");
		httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		// httpServletResponse.setHeader("Content-Disposition", "inline; filename=daily_report.xlxs");
		httpServletResponse.setHeader("Content-Disposition", "attachment; filename=detailed_report.xlsx");
		final Map<Long, String> ticketsMap = ticketDetailsService.getAllTickets().stream().collect(Collectors.toMap(TicketDetailsDTO::getId, TicketDetailsDTO::getName));
		final List<VisitorsTypeDTO> visitorsList = visitorTypeService.getAllVisitorTypes();
		final Map<Long, String> visitorsSingleMap = visitorsList.stream().filter(visitorsTypeDTO -> GroupType.SINGLE.equals(visitorsTypeDTO.getGroupType())).collect(Collectors.toMap(VisitorsTypeDTO::getId, VisitorsTypeDTO::getName));
		final Map<Long, String> visitorsComboMap = visitorsList.stream().filter(visitorsTypeDTO -> GroupType.COMBO.equals(visitorsTypeDTO.getGroupType())).collect(Collectors.toMap(VisitorsTypeDTO::getId, VisitorsTypeDTO::getName));
		final Map<Long, String> parkingsMap = parkingService.getParkingDetails().stream().collect(Collectors.toMap(ParkingDetailsDTO::getId, ParkingDetailsDTO::getName));
		final DailyReportExcel dailyReportExcel = new DailyReportExcel(ticketBillRowService.getDetailedReport(year), ticketsMap, visitorsSingleMap, visitorsComboMap, parkingsMap, httpServletResponse);
	}

	@PostMapping(value = "/detailed-report/xlsx", produces = MediaType.APPLICATION_PDF_VALUE)
	public String postRequestReportXLSX(@RequestParam String startDateTime, @RequestParam String endDateTime, HttpServletResponse httpServletResponse) throws IOException {
		if (!(startDateTime.isEmpty() || endDateTime.isEmpty())) {
			final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			try {
				// httpServletResponse.setContentType("application/xlsx");
				httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
				// httpServletResponse.setHeader("Content-Disposition", "inline; filename=daily_report.xlxs");
				httpServletResponse.setHeader("Content-Disposition", "attachment; filename=detailed_report.xlsx");
				final Map<Long, String> ticketsMap = ticketDetailsService.getAllTickets().stream().collect(Collectors.toMap(TicketDetailsDTO::getId, TicketDetailsDTO::getName));
				final List<VisitorsTypeDTO> visitorsList = visitorTypeService.getAllVisitorTypes();
				final Map<Long, String> visitorsSingleMap = visitorsList.stream().filter(visitorsTypeDTO -> GroupType.SINGLE.equals(visitorsTypeDTO.getGroupType())).collect(Collectors.toMap(VisitorsTypeDTO::getId, VisitorsTypeDTO::getName));
				final Map<Long, String> visitorsComboMap = visitorsList.stream().filter(visitorsTypeDTO -> GroupType.COMBO.equals(visitorsTypeDTO.getGroupType())).collect(Collectors.toMap(VisitorsTypeDTO::getId, VisitorsTypeDTO::getName));
				final Map<Long, String> parkingsMap = parkingService.getParkingDetails().stream().collect(Collectors.toMap(ParkingDetailsDTO::getId, ParkingDetailsDTO::getName));
				final DailyReportExcel dailyReportExcel = new DailyReportExcel(ticketBillRowService.getDetailedReport(simpleDateFormat.parse(startDateTime), simpleDateFormat.parse(endDateTime)), ticketsMap, visitorsSingleMap, visitorsComboMap, parkingsMap, httpServletResponse);
			}
			catch (ParseException parseException) {
				parseException.printStackTrace();
			}
		}
		return "redirect:xlsx";
	}
/*
	@GetMapping(path = "/year-wise/{year}")
	public String doDetailedReport(@PathVariable("year") Short year, Map<String, Object> attributes) {
		List<TicketDailyReport> ticketDailyReports = ticketBillRowRepository.getDailyReportDetails(year);
		attributes.put("startDateTime", "");
		attributes.put("endDateTime", "");
		attributes.put("targetDate", year);

		final Map<Long, String> ticketsMap = ticketDetailsService.getAllTickets().stream().collect(Collectors.toMap(TicketDetailsDTO::getId, TicketDetailsDTO::getName));
		final List<VisitorsTypeDTO> vistitorsList = visitorTypeService.getAllActiveVisitorTypes();
		final Map<Long, String> visitorsSingleMap = vistitorsList.stream().filter(visitorsTypeDTO -> GroupType.SINGLE.equals(visitorsTypeDTO.getGroupType())).collect(Collectors.toMap(VisitorsTypeDTO::getId, VisitorsTypeDTO::getName));
		final Map<Long, String> visitorsComboMap = vistitorsList.stream().filter(visitorsTypeDTO -> GroupType.COMBO.equals(visitorsTypeDTO.getGroupType())).collect(Collectors.toMap(VisitorsTypeDTO::getId, VisitorsTypeDTO::getName));
		final Map<Long, String> parkingsMap = parkingService.getParkingDetails().stream().collect(Collectors.toMap(ParkingDetailsDTO::getId, ParkingDetailsDTO::getName));
		// final Map<Long, String> visitorsMap = visitorTypeService.getAllVisitorTypes().stream().collect(Collectors.toMap(VisitorsTypeDTO::getId, VisitorsTypeDTO::getName));

		attributes.put("ticketsName", ticketsMap.values().stream().collect(Collectors.toList()));
		attributes.put("visitorsSingleName", visitorsSingleMap.values().stream().collect(Collectors.toList()));
		attributes.put("visitorsComboName", visitorsComboMap.values().stream().collect(Collectors.toList()));
		attributes.put("parkingsName", parkingsMap.values().stream().collect(Collectors.toList()));
		double []grandTotal = new double[1];
		attributes.put("bills", DetailedReport.arrange(ticketDailyReports, ticketsMap, visitorsSingleMap, visitorsComboMap, parkingsMap, grandTotal));
		attributes.put("ticketSerials", new int[2]);
		attributes.put("grandTotal", grandTotal[0]);
		return "reports/date";
	}

	@PostMapping(value = "/year-wise")
	public String redirect(@RequestParam Short year) {
		return "redirect:/report/year-wise/" + year;
	}

	@PostMapping(value = "/year-wise/{year}", produces = MediaType.APPLICATION_PDF_VALUE)
	public void generateSheet(@PathVariable("year") Short year, HttpServletResponse httpServletResponse) throws IOException {
		// httpServletResponse.setContentType("application/xlsx");
		httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		// httpServletResponse.setHeader("Content-Disposition", "inline; filename=daily_report.xlxs");
		httpServletResponse.setHeader("Content-Disposition", "attachment; filename=daily_report.xlsx");
		List<TicketDailyReport> ticketDailyReports = ticketBillRowRepository.getDailyReportDetails(year);
		final Map<Long, String> ticketsMap = ticketDetailsService.getAllTickets().stream().collect(Collectors.toMap(TicketDetailsDTO::getId, TicketDetailsDTO::getName));
		final List<VisitorsTypeDTO> visitorsList = visitorTypeService.getAllVisitorTypes();
		final Map<Long, String> visitorsSingleMap = visitorsList.stream().filter(visitorsTypeDTO -> GroupType.SINGLE.equals(visitorsTypeDTO.getGroupType())).collect(Collectors.toMap(VisitorsTypeDTO::getId, VisitorsTypeDTO::getName));
		final Map<Long, String> visitorsComboMap = visitorsList.stream().filter(visitorsTypeDTO -> GroupType.COMBO.equals(visitorsTypeDTO.getGroupType())).collect(Collectors.toMap(VisitorsTypeDTO::getId, VisitorsTypeDTO::getName));
		final Map<Long, String> parkingsMap = parkingService.getParkingDetails().stream().collect(Collectors.toMap(ParkingDetailsDTO::getId, ParkingDetailsDTO::getName));
		final DailyReportExcel dailyReportExcel = new DailyReportExcel(ticketDailyReports, ticketsMap, visitorsSingleMap, visitorsComboMap, parkingsMap, httpServletResponse);
	}
*/
}
