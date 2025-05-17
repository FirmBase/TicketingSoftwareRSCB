package com.rsc.bhopal.utills;

import com.itextpdf.text.Rectangle;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;
import com.rsc.bhopal.dtos.BillSummarize;
import com.rsc.bhopal.dtos.PrintAdjustDTO;
import com.rsc.bhopal.dtos.TicketBillDTO;
import com.rsc.bhopal.utills.PDFGenerateObject;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PDFGenerate {
	private final int PPI = 72;
	private final int FLOATING_FONT_SIZE = 10;

	public PDFGenerate(final List<PrintAdjustDTO> printAdjustDTOs, final TicketBillDTO ticketBillDTO, final HttpServletResponse httpServletResponse) {
		httpServletResponse.setContentType("application/pdf");
		httpServletResponse.setHeader("Content-Disposition", "inline; filename=document.pdf");

		// final BillSummarize billSummarize = CommonUtills.convertJSONToObject(ticketBillDTO.getTicketPayload(), BillSummarize.class);
		final BillSummarize billSummarize = ticketBillDTO.getBillSummarize();
		log.debug("Bill Summarize for printing: " + billSummarize);

		PDFGenerateObject pdfGenerate = null;
		float frame_width = 0, frame_height = 0;

		for (final PrintAdjustDTO printAdjustDTO: printAdjustDTOs) {
			if (printAdjustDTO.getTitle().endsWith("frame")) {
				frame_width = printAdjustDTO.getWidth();
				frame_height = printAdjustDTO.getHeight();
				pdfGenerate = new PDFGenerateObject(centimetersToPixels(printAdjustDTO.getWidth()), centimetersToPixels(printAdjustDTO.getHeight()));
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
	}

	public float centimetersToPixels(float cm) {
		return cm * this.PPI / 2.54f;
	}
}

@Slf4j
class PDFGenerateObject {
	private Document document;
	private PdfWriter pdfWriter;

	public PDFGenerateObject(final float width, final float height) {
		document = new Document(new Rectangle(width, height));
	}

	public void open(HttpServletResponse httpServletResponse) {
		try {
			pdfWriter = PdfWriter.getInstance(document, httpServletResponse.getOutputStream());
			document.open();
		}
		catch (DocumentException ex) {
			log.debug("Error in PDF Document: " + ex.getMessage());
		}
		catch (IOException ex) {
			log.debug(ex + ex.getMessage());
		}
	}

	public void open(final String path) {
		try {
			pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(path));
			document.open();
		}
		catch (DocumentException ex) {
			log.debug("Error in PDF Document: " + ex.getMessage());
		}
		catch (FileNotFoundException ex) {
			log.debug(ex + ex.getMessage());
		}
	}

	public void addTextChunk(final String text, final int fontSize) {
		try {
			Font font = FontFactory.getFont(FontFactory.COURIER, fontSize, BaseColor.BLACK);
			Chunk chunk = new Chunk(text, font);
			document.add(chunk);
		}
		catch(Exception ex) {
			log.debug("Error in adding text chunk: " + ex.getMessage());
		}
	}

	public void addFloatingText(final String text, final int fontSize, final int x, final int y) {
		try {
			PdfContentByte pdfContentByte = pdfWriter.getDirectContent();
			pdfContentByte.beginText();
			BaseFont baseFont = BaseFont.createFont(BaseFont.HELVETICA, BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
			pdfContentByte.setFontAndSize(baseFont, fontSize);
			pdfContentByte.setTextMatrix(x, y);
			pdfContentByte.showText(text);
			pdfContentByte.endText();
		}
		catch(DocumentException | IOException ex) {
			log.debug("Error adding floating text: " + ex.getMessage());
		}
	}

	public void close() {
		document.close();
	}
}
