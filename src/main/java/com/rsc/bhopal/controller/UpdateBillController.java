package com.rsc.bhopal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.rsc.bhopal.service.TicketsRatesService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/bill")
@Slf4j
public class UpdateBillController {
	@Autowired
	private TicketsRatesService ticketsRatesService;
	protected UpdateBillController() {
		log.debug("Bill update requested.");
	}

	@PostMapping("update")
	public String updateBill(@RequestBody final String ticketSchema) {
		return "{success: true, Got: " + ticketSchema + "}";
	}

	@GetMapping("/")
	public String getMethodName() {
		// ticketDetails.getAllTickets().forEach(System.out::println);
		ticketsRatesService.getAllTicketRates().forEach(System.out::println);
		return "<html><head><title></title></head><body bgcolor=\"black\" text=\"white\">hello</body></html>";
	}
}
