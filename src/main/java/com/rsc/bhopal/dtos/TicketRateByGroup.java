package com.rsc.bhopal.dtos;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TicketRateByGroup {
	private List<String> groups;
	private List<TicketRate> ticketRates = new ArrayList<TicketRate>();
}

