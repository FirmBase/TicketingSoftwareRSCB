package com.rsc.bhopal.dtos;

import java.util.Date;
import java.math.BigInteger;

import lombok.Data;

@Data
public class TicketBillDTO {
	private Long id;
	private Date generatedAt;
	private int persons;
	private Double totalBill;
	private String ticketPayload;
	private BigInteger ticketSerial;
	private Character billSeries;
	private BigInteger billSerial;
	private Boolean cancelledStatus;
	private String generatedBy;
	private String institution;
	private String remark;
	private BillSummarize billSummarize;
	// List<BillSummary> billSummary;
}
