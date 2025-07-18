package com.rsc.bhopal.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.rsc.bhopal.dtos.TicketBillDTO;
import com.rsc.bhopal.service.TicketBillService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/recent-tickets")
public class TicketController {

	@Autowired
	private TicketBillService ticketBillService;

	@GetMapping(path = "")
	public String recentTickets() {
		return "redirect:recent-tickets/10";
	}

	@GetMapping(path = "/")
	public String recentTickets_() {
		return "redirect:../recent-tickets/10";
	}

	@GetMapping(path = {"/{rows}", "/{rows}"})
	public String recentTickets(@PathVariable(name = "rows", required = false) Integer rows, Map<String, Object> mapAttributes) {
		rows = rows == null ? 10 : rows;
		List<TicketBillDTO> generatedTickets = ticketBillService.getRecentTickets(rows);
		// log.debug("Bill fetched: " + generatedTickets.size());
		mapAttributes.put("tickets", generatedTickets);
		return "tickets/recent";
	}

	@PostMapping(path = "/cancel")
	public String cancelTicketBill(@RequestParam String ticketBillId) {
		// log.debug("Request for ticket cancellation on Bill ID: " + ticketBillId);
		ticketBillService.cancelTicketBill(Long.parseLong(ticketBillId));
		return "redirect:recent-tickets/10";
	}
}
