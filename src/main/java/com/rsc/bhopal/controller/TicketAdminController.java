package com.rsc.bhopal.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rsc.bhopal.dtos.TicketDetailsDTO;
import com.rsc.bhopal.service.TicketDetailsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/manage/tickets")
public class TicketAdminController {
	@Autowired
	TicketDetailsService ticketDetailsService;

	@GetMapping(path = "/add")
	public String addGroup(Map<String,Object> attributes) {			
		List<TicketDetailsDTO> tickets = ticketDetailsService.getAllTickets();
		attributes.put("tickets", tickets);
		return "tickets/add";
	}

	@PostMapping(path = "/add")
	public String addGroup(@RequestParam("name") String ticketName,Principal user) throws JsonProcessingException {
		ticketDetailsService.addTicket(ticketName,user.getName());        
		return "redirect:/manage/tickets/add";
	}

	@PostMapping("/status")
	public String changeStatus(@RequestParam("ticketId") Long ticketId) {
		ticketDetailsService.changeTicketStatus(ticketId);	    
		return "redirect:/manage/tickets/add";
	}
}
