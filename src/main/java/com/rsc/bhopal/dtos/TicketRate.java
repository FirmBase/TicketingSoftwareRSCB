package com.rsc.bhopal.dtos;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

@Data
public class TicketRate {
	private Long ticketId;
	private String ticketName;
	private Map<Long, Float> prices = new HashMap<Long, Float>();
}
