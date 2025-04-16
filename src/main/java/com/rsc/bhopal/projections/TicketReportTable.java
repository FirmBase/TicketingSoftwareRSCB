package com.rsc.bhopal.projections;

import java.math.BigInteger;

public interface TicketReportTable {
	public Long getId();

	public Long getSerialNo();

	public Long getTicketId();

	public String getTicketName();

	public Long getVisitorId();

	public String getVisitorName();

	public Integer getPersons();

	public Float getPrice();

	public Double getTotalSum();

	public BigInteger getTicketSerial();

	public Boolean getCancelledStatus();
}
