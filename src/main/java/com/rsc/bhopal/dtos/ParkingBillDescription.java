package com.rsc.bhopal.dtos;

import lombok.Data;

@Data
public class ParkingBillDescription {

	private String desc;
	private String groupName;
	private int count;
	private float perCharge;
	private float sum;
}
