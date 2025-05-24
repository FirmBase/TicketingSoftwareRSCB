package com.rsc.bhopal.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.rsc.bhopal.dtos.BillSummaryDateRange;
import com.rsc.bhopal.dtos.TicketBillSummaryDTO;
import com.rsc.bhopal.dtos.TicketDetailsDTO;
import com.rsc.bhopal.dtos.VisitorsTypeDTO;
import com.rsc.bhopal.enums.GroupType;
import com.rsc.bhopal.service.ParkingService;
import com.rsc.bhopal.service.TicketDetailsService;
import com.rsc.bhopal.service.TicketSummaryService;
import com.rsc.bhopal.service.VisitorTypeService;
import com.rsc.bhopal.utills.CommonUtills;
import com.rsc.bhopal.utills.RSCDateFormat;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class HomeController {

	@Autowired
	private TicketDetailsService ticketDetails;

	@Autowired
	private VisitorTypeService visitorDetails;

	@Autowired
	private ParkingService parkingService;

	@Autowired
	private TicketSummaryService ticketSummaryService;

	@GetMapping(path = {"/","/home","/home/{variable}"} )
	public String hello(Map<String, Object> attributes) {
		List<TicketDetailsDTO> tickets = ticketDetails.getAllActiveTickets();
		List<VisitorsTypeDTO> visitors = visitorDetails.getAllActiveVisitorTypes();
		String redirectString = "";
		if(tickets.size() == 0 || visitors.size() == 0) {
			if(tickets.size() == 0) {
				redirectString="redirect:/manage/tickets/add";
			}
			else if(visitors.size() == 0) {
				redirectString="redirect:/manage/groups/add";
			}
		}
		else {
			// attributes.put("tickets1", tickets.subList(0, 8));
			// attributes.put("tickets2", tickets.subList(8, tickets.size()));

			// float ticketGroups = (float) tickets.size() / 8f;
			// attributes.put("ticketGroups", ticketGroups);

			attributes.put("tickets", tickets);

			attributes.put("groups", visitors.stream().filter(visitorType->
				GroupType.SINGLE.equals(visitorType.getGroupType())
				).collect(Collectors.toList()));

			visitors.forEach(visitorType -> {
				if (GroupType.SINGLE.equals(visitorType.getGroupType()) && visitorType.getIsDefault()) {
					attributes.put("defaultpersonvalue", visitorType.getMinMembers());
				}
			});

			attributes.put("familyGroups", visitors.stream().filter(visitorType->
				GroupType.COMBO.equals(visitorType.getGroupType())
				).collect(Collectors.toList()));

			attributes.put("parkings", parkingService.getActiveParkingDetails());
			redirectString = "employee/home";
		}
		return redirectString;
	}

/*
	@GetMapping(path = "/home/tickets-summary")
	public @ResponseBody List<TicketSummary> getTicketSummary(Map<String, Object> attributes) {
		String currentDate = CommonUtills.convertDateToString(new Date(), RSCDateFormat.YYYY_MM_DD);
		List<TicketSummary> summaries = ticketBillService.getTicketSummary("2024-09-22", "2024-09-23");
		summaries.forEach(summa->{
			// System.out.println(summa.getTICKET());
		});
		List<TicketSummaryDTO> ticketSummaryDTOs = ticketBillService.getTicketSummaryDTOs("2024-09-22", "2024-09-23");
		attributes.put("summaries", ticketSummaryDTOs);
		// return "summary/summary";
		// return ticketSummaryDTOs;
		return summaries;
	}
*/

	@GetMapping(path = "/home/summary")
	public String getAll(Map<String, Object> attributes) {
		final String currentDate = CommonUtills.convertDateToString(new Date(), RSCDateFormat.YYYY_MM_DD);
		attributes.put("startDate", "");
		attributes.put("endDate", currentDate);
		// return ticketSummaryService.getTicketSummaryCountByTicketsAndGroups("2024-09-22", "2024-09-23");
		// log.debug("Ticket: " + ticketSummaryService.getAllTicketSummary());
		// attributes.put("summaries", ticketSummaryService.getTicketSummaryCountByTicketsAndGroups("2024-09-22", "2024-09-23"));
		List<TicketBillSummaryDTO> ticketBillSummaryDTOs = new java.util.ArrayList<TicketBillSummaryDTO>();
		long grandCount = 0;
		double grandTotal = 0;
		for (com.rsc.bhopal.projections.TicketSummary billRow: ticketSummaryService.getTicketSummaryCountByTicketsAndGroups("2000-01-01", currentDate)) {
			TicketBillSummaryDTO ticketBillSummaryDTO = new TicketBillSummaryDTO();
			// BeanUtils.copyProperties(billRow, ticketBillSummaryDTO);
			ticketBillSummaryDTO.setCount(billRow.getCOUNT());
			ticketBillSummaryDTO.setTicketName(billRow.getTICKET());
			ticketBillSummaryDTO.setGroupName(billRow.getGROUP_());
			ticketBillSummaryDTO.setTotal(billRow.getTOTAL() * billRow.getCOUNT());
			grandCount += billRow.getCOUNT();
			grandTotal += billRow.getTOTAL() * billRow.getCOUNT();
			ticketBillSummaryDTOs.add(ticketBillSummaryDTO);
		}
		attributes.put("billRows", ticketBillSummaryDTOs);
		attributes.put("grandCount", grandCount);
		attributes.put("grandTotal", grandTotal);
		// return ticketSummaryService.getTicketSummaryCountByTicketsAndGroups("2024-09-22", "2024-09-23");
		return "summary/summary";
		// return ticketBillSummaryDTOs;
	}

	@PostMapping(path = "/home/summary")
	public String getSpecificBillSummary(@ModelAttribute BillSummaryDateRange billSummaryDateRange, Map<String, Object> attributes) {
		// log.debug("input range: " + billSummaryDateRange);
		attributes.put("startDate", billSummaryDateRange.startDate);
		attributes.put("endDate", billSummaryDateRange.endDate);
		List<TicketBillSummaryDTO> ticketBillSummaryDTOs = new java.util.ArrayList<TicketBillSummaryDTO>();
		long grandCount = 0;
		double grandTotal = 0;
		for (com.rsc.bhopal.projections.TicketSummary billRow: ticketSummaryService.getTicketSummaryCountByTicketsAndGroups(billSummaryDateRange.startDate, billSummaryDateRange.endDate)) {
			TicketBillSummaryDTO ticketBillSummaryDTO = new TicketBillSummaryDTO();
			// BeanUtils.copyProperties(billRow, ticketBillSummaryDTO);
			ticketBillSummaryDTO.setCount(billRow.getCOUNT());
			ticketBillSummaryDTO.setTicketName(billRow.getTICKET());
			ticketBillSummaryDTO.setGroupName(billRow.getGROUP_());
			ticketBillSummaryDTO.setTotal(billRow.getTOTAL() * billRow.getCOUNT());
			grandCount += billRow.getCOUNT();
			grandTotal += billRow.getTOTAL() * billRow.getCOUNT();
			ticketBillSummaryDTOs.add(ticketBillSummaryDTO);
		}
		attributes.put("billRows", ticketBillSummaryDTOs);
		attributes.put("grandCount", grandCount);
		attributes.put("grandTotal", grandTotal);
		return "summary/summary";
	}
}
