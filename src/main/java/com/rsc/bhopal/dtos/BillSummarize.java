package com.rsc.bhopal.dtos;

import java.math.BigInteger;
import java.util.List;


import lombok.Data;

@Data
public class BillSummarize {
	private List<BillDescription> billDescription;
	private List<ParkingBillDescription> parkingBillDescription;
	private boolean comboCase;
	private BigInteger ticketSerial;
	// private Long ticketSerial;
	// private long ticketSerial;
	private boolean cancelledStatus;
}
