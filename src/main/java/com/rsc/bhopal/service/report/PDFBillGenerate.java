package com.rsc.bhopal.service.report;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rsc.bhopal.service.ApplicationConstantService;
import com.rsc.bhopal.service.TicketBillService;
import com.rsc.bhopal.utills.PDFGenerate;

import jakarta.servlet.http.HttpServletResponse;

@Service
public class PDFBillGenerate {
	@Autowired
	private ApplicationConstantService applicationConstantService;

	@Autowired
	private TicketBillService ticketBillService;

	public void generateBill(final long billId, final HttpServletResponse httpServletResponse) {
		final PDFGenerate pdfGenerate = new PDFGenerate(applicationConstantService.getAllCurrentPrintCoordinate(), ticketBillService.findById(billId), httpServletResponse);
	}
}
