package com.rsc.bhopal.dtos.report;

import java.math.BigInteger;

import lombok.Data;

@Data
public class RSCBReportGroup {
	private Long groupId;
	private String groupName;
	private Integer personCount;
	private Integer count;
	private Float price;
	private BigInteger ticketSerial;
}
