package com.rsc.bhopal.utills;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.Rectangle;
// import com.itextpdf.awt.geom.Rectangle;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PDFGenerate {
	private Document document;
	private PdfWriter pdfWriter;

	public PDFGenerate(final float width, final float height) {
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

	public static void main(String[] args) {
		PDFGenerate pdfGenerate = new PDFGenerate(355, 210);
		final short left_margin = 50;
		pdfGenerate.open("iTextPDF.pdf");
		// pdfGenerate.addTextChunk("Hello World!", 16);
		pdfGenerate.addFloatingText("Ticket Serial:", 10, left_margin, 130);
		pdfGenerate.addFloatingText("29987", 10, left_margin + 70, 130);

		pdfGenerate.addFloatingText("Category:", 10, left_margin, 102);
		pdfGenerate.addFloatingText("Others", 10, left_margin + 70, 102);

		pdfGenerate.addFloatingText("Person Count:", 10, left_margin, 88);
		pdfGenerate.addFloatingText("8", 10, left_margin + 70, 88);

		pdfGenerate.addFloatingText("Rate:", 10, left_margin, 72);
		pdfGenerate.addFloatingText("", 10, left_margin + 70, 72);

		pdfGenerate.addFloatingText("Total amount:", 10, left_margin, 56);
		pdfGenerate.addFloatingText("Rs. 0", 10, left_margin + 70, 56);

		pdfGenerate.addFloatingText("Date:", 10, 200, 130);
		pdfGenerate.addFloatingText("19, Jul 24", 10, 230, 130);

		pdfGenerate.addFloatingText("Time:", 10, 200, 114);
		pdfGenerate.addFloatingText("09:14 AM", 10, 230, 114);
		pdfGenerate.close();
	}

	public void close() {
		document.close();
	}
}
