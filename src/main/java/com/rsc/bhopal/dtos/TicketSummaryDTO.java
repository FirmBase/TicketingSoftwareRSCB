package com.rsc.bhopal.dtos;

import lombok.Data;

@Data
public class TicketSummaryDTO {
	Long id;

	Long totalSum;

	Long billId;

	private TicketsRatesMasterDTO ticketsRatesMasterDTO;
}
