package com.rsc.bhopal.dtos;

import java.math.BigInteger;
import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class TicketSearchDTO {
	private String searchBy;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date ticketAt;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date ticketFrom;
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date ticketTo;

	private Character billSeries;
	private BigInteger billSerial;
}
