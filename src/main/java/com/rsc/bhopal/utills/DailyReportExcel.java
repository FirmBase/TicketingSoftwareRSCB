package com.rsc.bhopal.utills;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.rsc.bhopal.dtos.report.BillDate;
import com.rsc.bhopal.dtos.report.Group;
import com.rsc.bhopal.dtos.report.Parking;
import com.rsc.bhopal.dtos.report.Ticket;
import com.rsc.bhopal.projections.TicketDailyReport;
import com.rsc.bhopal.service.report.DetailedReport;

import jakarta.servlet.http.HttpServletResponse;

public class DailyReportExcel {
	private int columnCount, rowCount;

	public DailyReportExcel(List<TicketDailyReport> ticketDailyReports, Map<Long, String> ticketsMap, Map<Long, String> groupsSingleMap, Map<Long, String> groupsComboMap, Map<Long, String> parkingsMap, HttpServletResponse httpServletResponse) throws IOException {
		double []grandTotal = new double[1];
		LinkedHashMap<Date, BillDate> billDates = new DetailedReport().arrange(ticketDailyReports, ticketsMap, groupsSingleMap, groupsComboMap, parkingsMap, grandTotal);
		List<String> ticketsName = ticketsMap.values().stream().collect(Collectors.toList());
		List<String> groupsSingleName = groupsSingleMap.values().stream().collect(Collectors.toList());
		List<String> groupsComboName = groupsComboMap.values().stream().collect(Collectors.toList());
		List<String> parkingsName = parkingsMap.values().stream().collect(Collectors.toList());

		Workbook workbook = new XSSFWorkbook();
		Sheet sheet = workbook.createSheet("Daily Report");
		CellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

		// Header
		rowCount = 0;
		columnCount = 0;
		Row headerRow0 = sheet.createRow(rowCount++);
		Cell headerCell0 = headerRow0.createCell(columnCount++);
		headerCell0.setCellValue("Serial");
		headerCell0.setCellStyle(cellStyle);
		sheet.addMergedRegion(new CellRangeAddress(0, 1, columnCount - 1, columnCount - 1));
		Cell headerCell1 = headerRow0.createCell(columnCount++);
		headerCell1.setCellValue("Date");
		headerCell1.setCellStyle(cellStyle);
		sheet.addMergedRegion(new CellRangeAddress(0, 1, columnCount - 1, columnCount - 1));
		for (String ticketName: ticketsName) {
			Cell headerCell = headerRow0.createCell(columnCount);
			headerCell.setCellValue(ticketName);
			headerCell.setCellStyle(cellStyle);
			if (groupsSingleName.size() > 1)
				sheet.addMergedRegion(new CellRangeAddress(0, 0, columnCount, columnCount - 1 + groupsSingleName.size()));
			columnCount += groupsSingleName.size();
		}
		for (String groupComboName: groupsComboName) {
			Cell headerCell = headerRow0.createCell(columnCount++);
			headerCell.setCellValue(groupComboName);
			headerCell.setCellStyle(cellStyle);
			sheet.addMergedRegion(new CellRangeAddress(0, 1, columnCount - 1, columnCount - 1));
		}
		Cell headerCell__5 = headerRow0.createCell(columnCount++);
		headerCell__5.setCellValue("Parking");
		headerCell__5.setCellStyle(cellStyle);
		if (parkingsName.size() > 1)
			sheet.addMergedRegion(new CellRangeAddress(0, 0, columnCount - 1, columnCount - 2 + parkingsName.size()));
		columnCount += parkingsName.size();
		Cell headerCell___6 = headerRow0.createCell(columnCount - 1);
		headerCell___6.setCellValue("Amount");
		headerCell___6.setCellStyle(cellStyle);
		if (ticketsName.size() > 1)
			sheet.addMergedRegion(new CellRangeAddress(0, 0, columnCount - 1, columnCount - 2 + ticketsName.size()));

		// Sub-Header
		columnCount = 1;
		Row headerRow1 = sheet.createRow(rowCount);
		// Cell headerCell_0 = headerRow1.createCell(columnCount++);
		for (String ticketName: ticketsName) {
			for (String groupSingleName: groupsSingleName) {
				Cell headerCell = headerRow1.createCell(++columnCount);
				headerCell.setCellValue(groupSingleName);
				headerCell.setCellStyle(cellStyle);
			}
		}
		for (String groupComboName: groupsComboName) {
			Cell headerCell = headerRow1.createCell(++columnCount);
			headerCell.setCellValue(groupComboName);
			headerCell.setCellStyle(cellStyle);
		}
		for (String parkingName: parkingsName) {
			Cell headerCell = headerRow1.createCell(++columnCount);
			headerCell.setCellValue(parkingName);
			headerCell.setCellStyle(cellStyle);
		}
		for (String ticketName: ticketsName) {
			Cell headerCell = headerRow1.createCell(++columnCount);
			headerCell.setCellValue(ticketName);
			headerCell.setCellStyle(cellStyle);
		}

		// Body
		for (HashMap.Entry<Date, BillDate> billDate: billDates.entrySet()) {
			columnCount = 0;
			Row row = sheet.createRow(++rowCount);
			Cell cellSerial = row.createCell(columnCount++);
			cellSerial.setCellValue(rowCount - 1);
			Cell cellDate = row.createCell(columnCount++);
			cellDate.setCellValue(billDate.getKey().toString());
			for (HashMap.Entry<Long, Ticket> ticket: billDate.getValue().getTickets().entrySet()) {
				for (HashMap.Entry<Long, Group> group: ticket.getValue().getGroups().entrySet()) {
					Cell cell = row.createCell(columnCount++);
					cell.setCellValue(group.getValue().getQuantity());
				}
			}
			for (HashMap.Entry<Long, Group> group: billDate.getValue().getGroups().entrySet()) {
				Cell cell = row.createCell(columnCount++);
				cell.setCellValue(group.getValue().getQuantity());
			}
			for (HashMap.Entry<Long, Parking> parking: billDate.getValue().getParkings().entrySet()) {
				Cell cell = row.createCell(columnCount++);
				cell.setCellValue(parking.getValue().getTotalCount());
			}
			for (HashMap.Entry<Long, Ticket> ticket: billDate.getValue().getTickets().entrySet()) {
				Cell cell = row.createCell(columnCount++);
				cell.setCellValue(ticket.getValue().getTotalAmount());
			}
		}

		// Footer
		columnCount = 1;
		Row footerRow = sheet.createRow(++rowCount);
		for (String ticketName: ticketsName) {
			for (String groupsName: groupsSingleName) {
				Cell cell = footerRow.createCell(++columnCount);
				cell.setCellFormula("SUM(" + getColumnLetterFromIndex(columnCount) + "3:" + getColumnLetterFromIndex(columnCount) + rowCount + ")");
			}
		}
		for (String groupName: groupsComboName) {
			Cell cell = footerRow.createCell(++columnCount);
			// System.out.println("SUM(" + getColumnLetterFromIndex(columnCount) + "3:" + getColumnLetterFromIndex(columnCount) + rowCount + ")");
			cell.setCellFormula("SUM(" + getColumnLetterFromIndex(columnCount) + "3:" + getColumnLetterFromIndex(columnCount) + rowCount + ")");
		}
		for (String parkingName: parkingsName) {
			Cell cell = footerRow.createCell(++columnCount);
			// System.out.println("SUM(" + getColumnLetterFromIndex(columnCount) + "3:" + getColumnLetterFromIndex(columnCount) + rowCount + ")");
			cell.setCellFormula("SUM(" + getColumnLetterFromIndex(columnCount) + "3:" + getColumnLetterFromIndex(columnCount) + rowCount + ")");
		}
		for (String ticketName: ticketsName) {
			Cell cell = footerRow.createCell(++columnCount);
			cell.setCellFormula("SUM(" + getColumnLetterFromIndex(columnCount) + "3:" + getColumnLetterFromIndex(columnCount) + rowCount + ")");
		}

		// Footer border
		columnCount = 1;
		CellStyle footerCellStyleLeft = workbook.createCellStyle();
		footerCellStyleLeft.setBorderTop(BorderStyle.THICK);
		footerCellStyleLeft.setBorderBottom(BorderStyle.THICK);
		footerCellStyleLeft.setBorderLeft(BorderStyle.THICK);
		// footerCellStyleLeft.setBorderRight(BorderStyle.THIN);
		footerRow.getCell(++columnCount).setCellStyle(footerCellStyleLeft);
		CellStyle footerCellStyleMiddle = workbook.createCellStyle();
		footerCellStyleMiddle.setBorderTop(BorderStyle.THICK);
		footerCellStyleMiddle.setBorderBottom(BorderStyle.THICK);
		// footerCellStyleMiddle.setBorderLeft(BorderStyle.THIN);
		// footerCellStyleMiddle.setBorderRight(BorderStyle.THIN);
		for (int index = 0; index < (ticketsName.size() * groupsSingleName.size()) + groupsComboName.size() + parkingsName.size() + ticketsName.size() - 2; ++index) {
			footerRow.getCell(++columnCount).setCellStyle(footerCellStyleMiddle);
		}
		CellStyle footerCellStyleRight = workbook.createCellStyle();
		footerCellStyleRight.setBorderTop(BorderStyle.THICK);
		footerCellStyleRight.setBorderBottom(BorderStyle.THICK);
		// footerCellStyleRight.setBorderLeft(BorderStyle.THIN);
		footerCellStyleRight.setBorderRight(BorderStyle.THICK);
		footerRow.getCell(++columnCount).setCellStyle(footerCellStyleRight);

		try (OutputStream outputStream = httpServletResponse.getOutputStream()) {
			workbook.write(outputStream);
		}
		workbook.close();
	}

	private String getColumnLetterFromIndex(int columnIndex) {
		StringBuilder columnName = new StringBuilder();
		while (columnIndex >= 0) {
            columnName.insert(0, (char) ('A' + (columnIndex % 26)));
            columnIndex = (columnIndex / 26) - 1;
        }
		return columnName.toString();
	}
}
