package com.rsc.bhopal.dtos;

import lombok.Data;

@Data
public class TicketBillRowDTO {
	private Long id;

	private Float totalSum;

	private TicketBillDTO ticketBillDTO;

	private TicketsRatesMasterDTO ticketsRatesMasterDTO;
}
