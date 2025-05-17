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

import com.rsc.bhopal.service.report.PDFBillGenerate;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequestMapping("/print")
@Slf4j
public class PDFGenerationController {
	@Autowired
	private PDFBillGenerate pdfBillGenerate;
/*
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
*/
	@GetMapping(value = "/generate-pdf", produces = MediaType.APPLICATION_PDF_VALUE)
	public void getMethodName(@RequestParam("bill-id") Long billId, HttpServletResponse httpServletResponse) {
		pdfBillGenerate.generateBill(billId, httpServletResponse);
	}

	@PostMapping(value = "/generate-pdf", produces = MediaType.APPLICATION_PDF_VALUE)
	public void postMethodName(@RequestParam Long billId, HttpServletResponse httpServletResponse) {
		pdfBillGenerate.generateBill(billId, httpServletResponse);
	}
}
