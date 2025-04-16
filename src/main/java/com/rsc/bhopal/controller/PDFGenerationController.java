package com.rsc.bhopal.controller;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.HashMap;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Chunk;
// import com.itextpdf.awt.geom.Rectangle;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.rsc.bhopal.dtos.BillSummarize;
import com.rsc.bhopal.dtos.BillSummaryDTO;
import com.rsc.bhopal.dtos.PrintAdjustDTO;
import com.rsc.bhopal.dtos.TicketBillDTO;
import com.rsc.bhopal.service.ApplicationConstantService;
import com.rsc.bhopal.service.TicketBillService;
import com.rsc.bhopal.utills.CommonUtills;
import com.rsc.bhopal.utills.PDFGenerate;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/print")
@Slf4j
public class PDFGenerationController {
	private final int PPI = 72;

	private final int FLOATING_FONT_SIZE = 10;

	@Autowired
	private ApplicationConstantService applicationConstantService;

	@Autowired
	private TicketBillService ticketBillService;

	@GetMapping(value = "/generate-pdf", produces = MediaType.APPLICATION_PDF_VALUE)
	public String getMethodName(HttpServletResponse response) {
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "inline; filename=document.pdf");

		PDFGenerate pdfGenerate = null;
		float frame_width = 0, frame_height = 0;

		for (final PrintAdjustDTO printAdjustDTO: applicationConstantService.getAllCurrentPrintCoordinate()) {
			if (printAdjustDTO.getTitle().endsWith("frame")) {
				frame_width = printAdjustDTO.getWidth();
				frame_height = printAdjustDTO.getHeight();
				if (pdfGenerate == null) {
					pdfGenerate = new PDFGenerate(centimetersToPixels(printAdjustDTO.getWidth()), centimetersToPixels(printAdjustDTO.getHeight()));
					pdfGenerate.open("C:\\Users\\Public\\Desktop\\Ticket PDF.pdf");
				}
			}
			else {
				final float top = centimetersToPixels(frame_height - printAdjustDTO.getTop());
				final float left = centimetersToPixels(printAdjustDTO.getLeft());
				if (pdfGenerate != null) {
					pdfGenerate.addFloatingText(printAdjustDTO.getText(), FLOATING_FONT_SIZE, Math.round(left), Math.round(top));
				}
			}
		}

		if (pdfGenerate != null) {
			pdfGenerate.close();
		}

		return "redirect:/recent-tickets/10";
	}

	@PostMapping(value = "/generate-pdf", produces = MediaType.APPLICATION_PDF_VALUE)
	public void postMethodName(@RequestParam Long billId, HttpServletResponse httpServletResponse) {
		httpServletResponse.setContentType("application/pdf");
		httpServletResponse.setHeader("Content-Disposition", "inline; filename=document.pdf");

		final List<PrintAdjustDTO> printAdjustDTOs = applicationConstantService.getAllCurrentPrintCoordinate();

		final TicketBillDTO ticketBillDTO = ticketBillService.findById(billId);
		log.debug("Printing for Bill ID " + billId + ": " + ticketBillService.findById(billId));

		// final BillSummarize billSummarize = CommonUtills.convertJSONToObject(ticketBillDTO.getTicketPayload(), BillSummarize.class);
		final BillSummarize billSummarize = ticketBillDTO.getBillSummarize();
		log.debug("Bill Summarize for printing: " + billSummarize);

		PDFGenerate pdfGenerate = null;
		float frame_width = 0, frame_height = 0;

		for (final PrintAdjustDTO printAdjustDTO: printAdjustDTOs) {
			if (printAdjustDTO.getTitle().endsWith("frame")) {
				frame_width = printAdjustDTO.getWidth();
				frame_height = printAdjustDTO.getHeight();
				pdfGenerate = new PDFGenerate(centimetersToPixels(printAdjustDTO.getWidth()), centimetersToPixels(printAdjustDTO.getHeight()));
				pdfGenerate.open(httpServletResponse);
			}
			else if (printAdjustDTO.getTitle().endsWith("serial")) {
				final int y = Math.round(centimetersToPixels(frame_height - printAdjustDTO.getTop()));
				final int x = Math.round(centimetersToPixels(printAdjustDTO.getLeft()));
				if (pdfGenerate != null) {
					pdfGenerate.addFloatingText(billSummarize.getTicketSerial().toString(), FLOATING_FONT_SIZE, x, y);
				}
			}
			else if (printAdjustDTO.getTitle().endsWith("serial")) {
				final int y = Math.round(centimetersToPixels(frame_height - printAdjustDTO.getTop()));
				final int x = Math.round(centimetersToPixels(printAdjustDTO.getLeft()));
				if (pdfGenerate != null) {
					pdfGenerate.addFloatingText(billSummarize.getTicketSerial().toString(), FLOATING_FONT_SIZE, x, y);
				}
			}
			else if (printAdjustDTO.getTitle().endsWith("category")) {
				final int y = Math.round(centimetersToPixels(frame_height - printAdjustDTO.getTop()));
				final int x = Math.round(centimetersToPixels(printAdjustDTO.getLeft()));
				if (pdfGenerate != null) {
					// pdfGenerate.addFloatingText(billSummarize.getTicketSerial().toString(), FLOATING_FONT_SIZE, x, y);
				}
			}
			else if (printAdjustDTO.getTitle().endsWith("person_count")) {
				final int y = Math.round(centimetersToPixels(frame_height - printAdjustDTO.getTop()));
				final int x = Math.round(centimetersToPixels(printAdjustDTO.getLeft()));
				if (pdfGenerate != null) {
					pdfGenerate.addFloatingText(String.valueOf(ticketBillDTO.getPersons()), FLOATING_FONT_SIZE, x, y);
				}
			}
			else if (printAdjustDTO.getTitle().endsWith("per_person")) {
				final int y = Math.round(centimetersToPixels(frame_height - printAdjustDTO.getTop()));
				final int x = Math.round(centimetersToPixels(printAdjustDTO.getLeft()));
				if (pdfGenerate != null) {
					pdfGenerate.addFloatingText("Rs. " + ticketBillDTO.getTotalBill() / ticketBillDTO.getPersons(), FLOATING_FONT_SIZE, x, y);
				}
			}
			else if (printAdjustDTO.getTitle().endsWith("total_amount")) {
				final int y = Math.round(centimetersToPixels(frame_height - printAdjustDTO.getTop()));
				final int x = Math.round(centimetersToPixels(printAdjustDTO.getLeft()));
				if (pdfGenerate != null) {
					pdfGenerate.addFloatingText("Rs. " + ticketBillDTO.getTotalBill(), FLOATING_FONT_SIZE, x, y);
				}
			}
			else if (printAdjustDTO.getTitle().endsWith("date")) {
				final int y = Math.round(centimetersToPixels(frame_height - printAdjustDTO.getTop()));
				final int x = Math.round(centimetersToPixels(printAdjustDTO.getLeft()));
				if (pdfGenerate != null) {
					pdfGenerate.addFloatingText(String.valueOf(new SimpleDateFormat("dd, MMM yy").format(ticketBillDTO.getGeneratedAt())), FLOATING_FONT_SIZE, x, y);
				}
			}
			else if (printAdjustDTO.getTitle().endsWith("time")) {
				final int y = Math.round(centimetersToPixels(frame_height - printAdjustDTO.getTop()));
				final int x = Math.round(centimetersToPixels(printAdjustDTO.getLeft()));
				if (pdfGenerate != null) {
					pdfGenerate.addFloatingText(String.valueOf(new SimpleDateFormat("hh:mm a").format(ticketBillDTO.getGeneratedAt())), FLOATING_FONT_SIZE, x, y);
				}
			}
		}

		billSummarize.getBillDescription().forEach(billDescription -> {});

		if (pdfGenerate != null) {
			pdfGenerate.close();
		}

		// return "redirect:/recent-tickets/10";
	}

	public float centimetersToPixels(float cm) {
		return cm * this.PPI / 2.54f;
	}
}
