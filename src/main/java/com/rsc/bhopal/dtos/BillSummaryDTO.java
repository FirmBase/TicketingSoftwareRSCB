package com.rsc.bhopal.dtos;

import lombok.Data;

@Data
public class BillSummaryDTO {
	
	private Long id;
	
	private int persons;
	
	private Float price;
	
	private String ticketName;
	
	private String GroupName;
	
}
