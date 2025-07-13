package com.rsc.bhopal.dtos;

import java.util.Date;

import lombok.Data;

@Data
public class ParkingDetailsDTO {
	private Long id;
	private String name;
	private String idDsec;
	private Date addedAt;
	private Boolean isActive;
   private TicketsRatesMasterDTO ticketsRatesMasterDTO;
}
