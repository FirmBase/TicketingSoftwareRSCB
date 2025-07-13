package com.rsc.bhopal.dtos;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class TicketSelectorDTO {
	private List<Long> tickets;
	private Long group;
	private Long familyGroup;
	private Integer persons;
	private String institution;
	private String remark;
	private List<ParkingCalDTO> parkings;
	private Boolean printBill;
	public TicketSelectorDTO(){
		this.tickets = new ArrayList<Long>();
	}
}
