package com.rsc.bhopal.dtos.report;

import java.util.HashMap;

import lombok.Data;

@Data
public class RSCBReportTicket {
	private Long ticketId;
	private String ticketName;

	private HashMap<Long, RSCBReportGroup> group;
	private int ticketCount;
	private double subTotal;
}
