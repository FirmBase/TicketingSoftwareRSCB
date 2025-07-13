package com.rsc.bhopal.dtos.report;

import java.util.HashMap;

import lombok.Data;

@Data
public class Ticket {
	private Long ticketId;
	private String ticketName;
	private HashMap<Long, Group> groups;
	private Integer totalQuantity;
	private Double totalAmount;
}
