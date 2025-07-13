package com.rsc.bhopal.dtos.report;

import java.math.BigInteger;

import lombok.Data;

@Data
public class Group {
	private Long groupId;
	private String groupName;
	private BigInteger ticketSerial;
	private Integer quantity;
	private Float amount;
}
