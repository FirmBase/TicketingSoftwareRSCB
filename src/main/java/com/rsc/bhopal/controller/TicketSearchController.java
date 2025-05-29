package com.rsc.bhopal.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rsc.bhopal.dtos.TicketBillDTO;
import com.rsc.bhopal.dtos.TicketSearchDTO;
import com.rsc.bhopal.service.TicketBillService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/ticket-search")
public class TicketSearchController {
	@Autowired
	private TicketBillService ticketBillService;

	@GetMapping(path = {"", "/"})
	public String getSearchTicketBills() {
		return "tickets/search";
	}

	@PostMapping(path = {"", "/"})
	public String postSearchTicketBills(@ModelAttribute TicketSearchDTO ticketSearchDTO, Model attributes) {
		List<TicketBillDTO> ticketBillDTOs = Arrays.asList(new TicketBillDTO[0]);
		if (ticketSearchDTO.getSearchBy() != null) {
			if (ticketSearchDTO.getSearchBy().equalsIgnoreCase("date")) {
				ticketBillDTOs = ticketBillService.getTicketBills(ticketSearchDTO.getTicketAt());
			}
			if (ticketSearchDTO.getSearchBy().equals("date-range")) {
				ticketBillDTOs = ticketBillService.getTicketBills(ticketSearchDTO.getTicketFrom(), ticketSearchDTO.getTicketTo());
			}
			else if (ticketSearchDTO.getSearchBy().equalsIgnoreCase("serial")) {
				ticketBillDTOs = ticketBillService.getTicketBills(ticketSearchDTO.getBillSeries(), ticketSearchDTO.getBillSerial());
			}
		}
		attributes.addAttribute("tickets", ticketBillDTOs);
		return "tickets/recent";
	}
}
