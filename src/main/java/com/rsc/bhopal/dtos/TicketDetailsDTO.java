package com.rsc.bhopal.dtos;

import java.util.Date;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TicketDetailsDTO {

	private Long id;

	private Long groupId;

	private String name;

	private Date addedAt;

	private String addedBy;

	// RATE
	// if rate master contains row with this group id and ticket id (is active=true)
	private Float price;
 
	// RATE if rate master contains row with this group id and ticket id (is active=true) then true  
	private Boolean checked;

	private Boolean isActive;

}
