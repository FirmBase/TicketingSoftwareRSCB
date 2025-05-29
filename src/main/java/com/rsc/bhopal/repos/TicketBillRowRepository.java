package com.rsc.bhopal.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rsc.bhopal.entity.TicketBillRow;
import com.rsc.bhopal.projections.TicketDailyReport;
import com.rsc.bhopal.projections.TicketReportTable;

import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface TicketBillRowRepository extends JpaRepository<TicketBillRow, Long> {

	@Query(value = "SELECT bill FROM TicketBillRow bill WHERE bill.rate.billType = \'TICKET\' AND bill.generatedTicket.generatedAt >= :startDateTime AND bill.generatedTicket.generatedAt <= :endDateTime", nativeQuery = false)
	public List<TicketBillRow> getTicketBillRowsAtDateTime(Timestamp startDateTime, Timestamp endDateTime);

	@Query(value = "WITH report_table AS (" +
				"	SELECT" +
				"		rsc_ts.rsc_ts_ticket_bill_rows.id, ROW_NUMBER() OVER (PARTITION BY rsc_ts.rsc_ts_ticket_master.ticket_name) AS serial_no," +
				"		rsc_ts.rsc_ts_ticket_bill_rows.total_sum, rsc_ts.rsc_ts_ticket_bill.persons," +
				"		rsc_ts.rsc_ts_ticket_bill_rows.bill_id, rsc_ts.rsc_ts_ticket_bill_rows.rate_master_id," +
				"		rsc_ts.rsc_ts_ticket_bill.ticket_serial, rsc_ts.rsc_ts_ticket_bill.cancelled_status," +
				"		rsc_ts.rsc_ts_ticket_rate_master.price, rsc_ts.rsc_ts_ticket_rate_master.ticket_id, rsc_ts.rsc_ts_ticket_rate_master.visitor_id," +
				"		rsc_ts.rsc_ts_ticket_master.ticket_name, rsc_ts.rsc_ts_visitor_type_master.visitor_name" +
				"	FROM" +
				"		rsc_ts.rsc_ts_ticket_bill_rows" +
				"	LEFT JOIN" +
				"		rsc_ts.rsc_ts_ticket_bill" +
				"	ON" +
				"		rsc_ts.rsc_ts_ticket_bill_rows.bill_id = rsc_ts.rsc_ts_ticket_bill.id" +
				"	LEFT JOIN" +
				"		rsc_ts.rsc_ts_ticket_rate_master" +
				"	ON" +
				"		rsc_ts.rsc_ts_ticket_bill_rows.rate_master_id = rsc_ts.rsc_ts_ticket_rate_master.id" +
				"	LEFT JOIN" +
				"		rsc_ts.rsc_ts_ticket_master" +
				"	ON" +
				"		rsc_ts.rsc_ts_ticket_rate_master.ticket_id = rsc_ts.rsc_ts_ticket_master.id" +
				"	LEFT JOIN" +
				"		rsc_ts.rsc_ts_visitor_type_master" +
				"	ON" +
				"		rsc_ts.rsc_ts_ticket_rate_master.visitor_id = rsc_ts.rsc_ts_visitor_type_master.id" +
				"	WHERE" +
				"		rsc_ts.rsc_ts_ticket_rate_master.bill_type = 'TICKET'" +
				"	ORDER BY" +
				"		serial_no ASC, rsc_ts.rsc_ts_ticket_bill.ticket_serial DESC" +
				"	)" +
				"SELECT id AS Id, serial_no AS SerialNo, ticket_id AS TicketId, ticket_name AS TicketName, visitor_id AS VisitorId, visitor_name AS VisitorName, total_sum AS TotalSum, persons AS Persons, price AS Price, ticket_serial AS TicketSerial, cancelled_status AS CancelledStatus FROM report_table", nativeQuery = true)
	public List<TicketReportTable> getOverallTicketSales();

	@Query(value = "WITH report_table AS (" +
				"	SELECT" +
				"		rsc_ts.rsc_ts_ticket_bill_rows.id, ROW_NUMBER() OVER (PARTITION BY rsc_ts.rsc_ts_ticket_master.ticket_name) AS serial_no," +
				"		rsc_ts.rsc_ts_ticket_bill_rows.total_sum, rsc_ts.rsc_ts_ticket_bill.persons," +
				"		rsc_ts.rsc_ts_ticket_bill_rows.bill_id, rsc_ts.rsc_ts_ticket_bill_rows.rate_master_id," +
				"		rsc_ts.rsc_ts_ticket_bill.ticket_serial, rsc_ts.rsc_ts_ticket_bill.cancelled_status," +
				"		rsc_ts.rsc_ts_ticket_rate_master.price, rsc_ts.rsc_ts_ticket_rate_master.ticket_id, rsc_ts.rsc_ts_ticket_rate_master.visitor_id," +
				"		rsc_ts.rsc_ts_ticket_master.ticket_name, rsc_ts.rsc_ts_visitor_type_master.visitor_name" +
				"	FROM" +
				"		rsc_ts.rsc_ts_ticket_bill_rows" +
				"	LEFT JOIN" +
				"		rsc_ts.rsc_ts_ticket_bill" +
				"	ON" +
				"		rsc_ts.rsc_ts_ticket_bill_rows.bill_id = rsc_ts.rsc_ts_ticket_bill.id" +
				"	LEFT JOIN" +
				"		rsc_ts.rsc_ts_ticket_rate_master" +
				"	ON" +
				"		rsc_ts.rsc_ts_ticket_bill_rows.rate_master_id = rsc_ts.rsc_ts_ticket_rate_master.id" +
				"	LEFT JOIN" +
				"		rsc_ts.rsc_ts_ticket_master" +
				"	ON" +
				"		rsc_ts.rsc_ts_ticket_rate_master.ticket_id = rsc_ts.rsc_ts_ticket_master.id" +
				"	LEFT JOIN" +
				"		rsc_ts.rsc_ts_visitor_type_master" +
				"	ON" +
				"		rsc_ts.rsc_ts_ticket_rate_master.visitor_id = rsc_ts.rsc_ts_visitor_type_master.id" +
				"	WHERE" +
				"		rsc_ts.rsc_ts_ticket_rate_master.bill_type = 'TICKET'" +
				"		AND" +
				"		rsc_ts.rsc_ts_ticket_bill.generated_at >= :startDateTime" +
				"		AND" +
				"		rsc_ts.rsc_ts_ticket_bill.generated_at <= :endDateTime" +
				"	ORDER BY" +
				"		serial_no ASC, rsc_ts.rsc_ts_ticket_bill.ticket_serial DESC" +
				"	)" +
				"SELECT id AS Id, serial_no AS SerialNo, ticket_id AS TicketId, ticket_name AS TicketName, visitor_id AS VisitorId, visitor_name AS VisitorName, total_sum AS TotalSum, persons AS Persons, price AS Price, ticket_serial AS TicketSerial, cancelled_status AS CancelledStatus FROM report_table", nativeQuery = true)
	public List<TicketReportTable> getOverallTicketSales(Timestamp startDateTime, Timestamp endDateTime);

	// Ticket Daily Report
	final String detailedReportQuery =
"WITH report AS (" +
"	WITH rate AS (" +
"		SELECT" +
"			rate.id AS rate_master_id, rate.bill_type, rate.is_active, rate.price, rate.parking_det_id, NULL AS parking_det_name, rate.ticket_id, ticket.ticket_name, rate.visitor_id, visitor.visitor_name, visitor.group_type" +
"		FROM" +
"			rsc_ts.rsc_ts_ticket_rate_master rate, rsc_ts.rsc_ts_ticket_master ticket, rsc_ts.rsc_ts_visitor_type_master visitor" +
"		WHERE" +
"			rate.bill_type = 'TICKET' AND rate.ticket_id = ticket.id AND rate.visitor_id = visitor.id" +
"		UNION" +
"		SELECT" +
"			rate.id As rate_master_id, rate.bill_type, rate.is_active, rate.price, rate.parking_det_id, parking.name AS parking_det_name, rate.ticket_id, NULL, rate.visitor_id, NULL, NULL" +
"		FROM" +
"			rsc_ts.rsc_ts_ticket_rate_master rate, rsc_ts.rsc_ts_parking_details parking" +
"		WHERE" +
"			rate.bill_type = 'PARKING' AND rate.parking_det_id = parking.id" +
"	)" +
"	SELECT" +
"		DENSE_RANK() OVER (ORDER BY DATE(receipt.generated_at)) AS date_serial, DATE(receipt.generated_at) AS bill_date," +
"		bill.id, bill.total_sum, bill.bill_id," +
"		receipt.persons, receipt.cancelled_status, receipt.ticket_serial, receipt.total_bill, receipt.generated_by," +
"		rate.*" +
"	FROM rsc_ts.rsc_ts_ticket_bill_rows bill" +
"	INNER JOIN rsc_ts.rsc_ts_ticket_bill receipt ON bill.bill_id = receipt.id" +
"	LEFT JOIN rate ON bill.rate_master_id = rate.rate_master_id" +
"	)" +
"SELECT report.* FROM report";

	@Query(value = detailedReportQuery + " WHERE YEAR(report.bill_date) = :yearSearch", nativeQuery = true)
	public List<TicketDailyReport> getDetailedReport(Short yearSearch);

	@Query(value = detailedReportQuery + " WHERE report.bill_date >= :startDateTime AND report.bill_date <= :endDateTime", nativeQuery = true)
	public List<TicketDailyReport> getDetailedReport(Date startDateTime, Date endDateTime);

	@Query(value = "SELECT bill FROM TicketBillRow bill WHERE bill.rate.billType = \'TICKET\'", nativeQuery = false)
	public List<TicketBillRow> getTicketBillRows();

	@Query(value = "SELECT bill.generatedTicket.ticketSerial FROM TicketBillRow bill WHERE bill.rate.billType = \'TICKET\' ORDER BY bill.generatedTicket.ticketSerial DESC", nativeQuery = false)
	public List<BigInteger> getTicketsSerialDec();

	@Query(value = "SELECT bill.generatedTicket.ticketSerial FROM TicketBillRow bill WHERE bill.rate.billType = \'TICKET\' AND bill.generatedTicket.generatedAt >= :startDateTime AND bill.generatedTicket.generatedAt <= :endDateTime ORDER BY bill.generatedTicket.ticketSerial DESC", nativeQuery = false)
	public List<BigInteger> getTicketsSerialDec(Timestamp startDateTime, Timestamp endDateTime);

	@Query(value = "SELECT bill FROM TicketBillRow bill WHERE bill.rate.billType = \'TICKET\' AND bill.generatedTicket.cancelledStatus = :cancelledStatus ORDER BY bill.generatedTicket.ticketSerial DESC", nativeQuery = false)
	public List<TicketBillRow> getCancelledStatusDec(boolean cancelledStatus);

	@Query(value = "SELECT bill FROM TicketBillRow bill WHERE bill.rate.billType = \'TICKET\' AND bill.generatedTicket.generatedAt >= :startDateTime AND bill.generatedTicket.generatedAt <= :endDateTime AND bill.generatedTicket.cancelledStatus = :cancelledStatus ORDER BY bill.generatedTicket.ticketSerial DESC", nativeQuery = false)
	public List<TicketBillRow> getCancelledStatusDec(Timestamp startDateTime, Timestamp endDateTime, boolean cancelledStatus);
}
