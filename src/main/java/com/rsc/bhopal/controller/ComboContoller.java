package com.rsc.bhopal.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rsc.bhopal.dtos.TicketDetailsDTOWrapper;
import com.rsc.bhopal.dtos.VisitorsTypeDTO;
import com.rsc.bhopal.service.TicketDetailsService;
import com.rsc.bhopal.service.TicketsRatesService;
import com.rsc.bhopal.service.VisitorTypeService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/manage/combo")
public class ComboContoller {
	@Autowired
	VisitorTypeService visitorTypeService;

	@Autowired
	TicketsRatesService ticketsRatesService;

	@Autowired
	TicketDetailsService ticketDetails;

	@GetMapping(path = "/add")
	public String addComboGroup(Map<String, Object> attributes, @RequestParam(required = false, defaultValue = "0") Long groupId) {

		List<VisitorsTypeDTO> comboVisitors = visitorTypeService.getComboVisitorTypes();
		// groupCategory
		// log.debug(comboVisitors.toString());
		attributes.put("comboVisitors", comboVisitors);
		attributes.put("groupId", groupId);

		if (groupId != 0) {
			attributes.put("tickets", ticketDetails.getAllTicketsByGroup(groupId));
		}
		return "combo/add";
	}

	@PostMapping(path = "/price/change")
	public String addComboPrice(@ModelAttribute TicketDetailsDTOWrapper tickets, Principal user) {
		Long groupCategory = tickets.getTickets().stream().mapToLong(ticket -> ticket.getGroupId()).findFirst().getAsLong();
		// tickets.getTickets().stream().forEach(System.out::println);
		ticketsRatesService.updateRateForComboTicket(tickets.getTickets(), user.getName());
		return "redirect:/manage/combo/add?groupId=" + groupCategory;
	}
}
