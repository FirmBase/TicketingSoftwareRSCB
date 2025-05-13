package com.rsc.bhopal.service.report;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.rsc.bhopal.dtos.TicketReportTableDTO;
import com.rsc.bhopal.dtos.report.RSCBReportGroup;
import com.rsc.bhopal.dtos.report.RSCBReportTicket;
import com.rsc.bhopal.exception.TicketRateNotMaintainedException;
import com.rsc.bhopal.service.TicketsRatesService;

public class RSCBDailyReport {
	@Autowired
	private TicketsRatesService ticketsRatesService;

	private double grandTotal = 0;

}
