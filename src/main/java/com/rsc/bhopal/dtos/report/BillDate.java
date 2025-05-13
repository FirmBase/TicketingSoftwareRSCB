package com.rsc.bhopal.dtos.report;

import java.util.Date;
import java.util.HashMap;

import lombok.Data;

@Data
public class BillDate {
	private Date date;
	private HashMap<Long, Parking> parkings;
	private HashMap<Long, Ticket> tickets;
	private Integer totalTickets;
	private Double totalAmount;
	private HashMap<Long, Group> groups;
}
