package com.rsc.bhopal.dtos.report;

import lombok.Data;

@Data
public class Parking {
	private Long parkingId;
	private String parkingName;
	private int totalCount;
	private Double totalRate;
}
