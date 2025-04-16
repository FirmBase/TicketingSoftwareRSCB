package com.rsc.bhopal.dtos;

import java.util.Date;

import com.rsc.bhopal.enums.BillType;

import lombok.Data;

@Data
public class TicketRevisedSummary {

	private Long id; 
		
	private String  ticketType;
	
	private String  visitorsType;
	
	private String  parkingDetails;
		
	private Float price;
	
	private Boolean isActive;
	
	private Integer revisionNo;
	
	private Float oldPrice;
	
	private boolean isPriceIncreased;	
	
	private Date revisedAt;
	
	private BillType billType;
	
    private String user;
    
    private boolean isParkingRate;
    
    private Float percentage;
	
}
