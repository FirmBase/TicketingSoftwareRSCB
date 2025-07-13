package com.rsc.bhopal.dtos;

import java.math.BigInteger;

import lombok.Data;

@Data
public class TicketReportTableDTO {
	private Long id;
	private Long serialNo;
	private Long ticketId;
	private String ticketName;
	private Long visitorId;
	private String visitorName;
	private Integer persons;
	private Float price;
	private Double totalSum;
	private BigInteger ticketSerial;
	private Boolean cancelledStatus;
}
