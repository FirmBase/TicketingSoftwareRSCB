package com.rsc.bhopal.dtos;

import com.rsc.bhopal.entity.ParkingDetails;
import com.rsc.bhopal.entity.RSCUser;
import com.rsc.bhopal.enums.BillType;

import lombok.Data;

@Data
public class TicketsRatesMasterDTO {

	private Long id; 

	private TicketDetailsDTO ticketType;

	private VisitorsTypeDTO visitorsType;

	private ParkingDetailsDTO parkingDetails;
	// private ParkingDetailsDTO parkingDetailsDTO;

	private Boolean isActive;

	private Integer revisionNo;

	private java.util.Date revisedAt;

	private Float price;

	private BillType billType;

	private RSCUser user;

	private TicketsRatesMasterDTO oldRateMaster;
}
