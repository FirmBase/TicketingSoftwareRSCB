package com.rsc.bhopal.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rsc.bhopal.dtos.TicketReportTableDTO;
import com.rsc.bhopal.entity.TicketBillRow;
import com.rsc.bhopal.projections.TicketDailyReport;
import com.rsc.bhopal.projections.TicketReportTable;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

@Repository
public interface TicketBillRowRepository extends JpaRepository<TicketBillRow, Long> {
	/*
	@Query(value = "SELECT\n" +
		"	bill\n" +
		"FROM\n" +
		"	TicketBillRow bill\n" +
		"LEFT JOIN\n" +
		"	bill.generatedTicket\n" +
		"LEFT JOIN\n" +
		"	bill.rate",
		nativeQuery = false)
	*/
	@Query(value = "SELECT bill FROM TicketBillRow bill WHERE bill.rate.billType = \'TICKET\' AND bill.generatedTicket.generatedAt BETWEEN :startDateTime AND :endDateTime", nativeQuery = false)
	public List<TicketBillRow> getTicketBillRowsAtDateTime(Timestamp startDateTime, Timestamp endDateTime);
	/*
	@Query(value = "SELECT\n" +
				"	*\n" +
				"FROM\n" +
				"	rsc_ts.rsc_ts_ticket_bill_rows\n" +
				"LEFT JOIN\n" +
				"	rsc_ts.rsc_ts_ticket_bill\n" +
				"ON\n" +
				"	rsc_ts.rsc_ts_ticket_bill_rows.bill_id = rsc_ts.rsc_ts_ticket_bill.id\n" +
				"LEFT JOIN\n" +
				"	rsc_ts.rsc_ts_ticket_rate_master\n" +
				"ON\n" +
				"	rsc_ts.rsc_ts_ticket_bill_rows.rate_master_id = rsc_ts.rsc_ts_ticket_rate_master.id\n" +
				"LEFT JOIN\n" +
				"	rsc_ts.rsc_ts_ticket_master\n" +
				"ON\n" +
				"	rsc_ts.rsc_ts_ticket_rate_master.ticket_id = rsc_ts.rsc_ts_ticket_master.id\n" +
				"LEFT JOIN\n" +
				"	rsc_ts.rsc_ts_visitor_type_master\n" +
				"ON" +
				"	rsc_ts.rsc_ts_ticket_rate_master.visitor_id = rsc_ts.rsc_ts_visitor_type_master.id\n" +
				"LEFT JOIN\n" +
				"	rsc_ts.rsc_ts_users\n" +
				"ON\n" +
				"	rsc_ts.rsc_ts_ticket_bill.generated_by = rsc_ts.rsc_ts_users.id AND rsc_ts.rsc_ts_ticket_rate_master.revised_by\n",
		nativeQuery = true)
	public List<TicketBillRow> getTicketBillRowsAtDateTime(String startDateTime, String endDateTime);
	*/

	@Query(value = "WITH report_table AS (\n" +
				"	SELECT\n" +
				"		rsc_ts.rsc_ts_ticket_bill_rows.id, ROW_NUMBER() OVER (PARTITION BY rsc_ts.rsc_ts_ticket_master.ticket_name) AS serial_no,\n" +
				"		rsc_ts.rsc_ts_ticket_bill_rows.total_sum, rsc_ts.rsc_ts_ticket_bill.persons,\n" +
				"		rsc_ts.rsc_ts_ticket_bill_rows.bill_id, rsc_ts.rsc_ts_ticket_bill_rows.rate_master_id,\n" +
				"		rsc_ts.rsc_ts_ticket_bill.ticket_serial, rsc_ts.rsc_ts_ticket_bill.cancelled_status,\n" +
				"		rsc_ts.rsc_ts_ticket_rate_master.price, rsc_ts.rsc_ts_ticket_rate_master.ticket_id, rsc_ts.rsc_ts_ticket_rate_master.visitor_id,\n" +
				"		rsc_ts.rsc_ts_ticket_master.ticket_name, rsc_ts.rsc_ts_visitor_type_master.visitor_name\n" +
				"	FROM\n" +
				"		rsc_ts.rsc_ts_ticket_bill_rows\n" +
				"	LEFT JOIN\n" +
				"		rsc_ts.rsc_ts_ticket_bill\n" +
				"	ON\n" +
				"		rsc_ts.rsc_ts_ticket_bill_rows.bill_id = rsc_ts.rsc_ts_ticket_bill.id\n" +
				"	LEFT JOIN\n" +
				"		rsc_ts.rsc_ts_ticket_rate_master\n" +
				"	ON\n" +
				"		rsc_ts.rsc_ts_ticket_bill_rows.rate_master_id = rsc_ts.rsc_ts_ticket_rate_master.id\n" +
				"	LEFT JOIN\n" +
				"		rsc_ts.rsc_ts_ticket_master\n" +
				"	ON\n" +
				"		rsc_ts.rsc_ts_ticket_rate_master.ticket_id = rsc_ts.rsc_ts_ticket_master.id\n" +
				"	LEFT JOIN\n" +
				"		rsc_ts.rsc_ts_visitor_type_master\n" +
				"	ON\n" +
				"		rsc_ts.rsc_ts_ticket_rate_master.visitor_id = rsc_ts.rsc_ts_visitor_type_master.id\n" +
				"	WHERE\n" +
				"		rsc_ts.rsc_ts_ticket_rate_master.bill_type = 'TICKET'\n" +
				// "		AND\n" +
				// "		rsc_ts.rsc_ts_ticket_bill.generated_at BETWEEN \"\" AND \"\"\n" +
				"	ORDER BY\n" +
				"		serial_no ASC, rsc_ts.rsc_ts_ticket_bill.ticket_serial DESC\n" +
				"	)\n" +
				"SELECT id AS Id, serial_no AS SerialNo, ticket_id AS TicketId, ticket_name AS TicketName, visitor_id AS VisitorId, visitor_name AS VisitorName, total_sum AS TotalSum, persons AS Persons, price AS Price, ticket_serial AS TicketSerial, cancelled_status AS CancelledStatus FROM report_table", nativeQuery = true)
	public List<TicketReportTable> getTicketsReportTable();

	@Query(value = "WITH report_table AS (\n" +
				"	SELECT\n" +
				"		rsc_ts.rsc_ts_ticket_bill_rows.id, ROW_NUMBER() OVER (PARTITION BY rsc_ts.rsc_ts_ticket_master.ticket_name) AS serial_no,\n" +
				"		rsc_ts.rsc_ts_ticket_bill_rows.total_sum, rsc_ts.rsc_ts_ticket_bill.persons,\n" +
				"		rsc_ts.rsc_ts_ticket_bill_rows.bill_id, rsc_ts.rsc_ts_ticket_bill_rows.rate_master_id,\n" +
				"		rsc_ts.rsc_ts_ticket_bill.ticket_serial, rsc_ts.rsc_ts_ticket_bill.cancelled_status,\n" +
				"		rsc_ts.rsc_ts_ticket_rate_master.price, rsc_ts.rsc_ts_ticket_rate_master.ticket_id, rsc_ts.rsc_ts_ticket_rate_master.visitor_id,\n" +
				"		rsc_ts.rsc_ts_ticket_master.ticket_name, rsc_ts.rsc_ts_visitor_type_master.visitor_name\n" +
				"	FROM\n" +
				"		rsc_ts.rsc_ts_ticket_bill_rows\n" +
				"	LEFT JOIN\n" +
				"		rsc_ts.rsc_ts_ticket_bill\n" +
				"	ON\n" +
				"		rsc_ts.rsc_ts_ticket_bill_rows.bill_id = rsc_ts.rsc_ts_ticket_bill.id\n" +
				"	LEFT JOIN\n" +
				"		rsc_ts.rsc_ts_ticket_rate_master\n" +
				"	ON\n" +
				"		rsc_ts.rsc_ts_ticket_bill_rows.rate_master_id = rsc_ts.rsc_ts_ticket_rate_master.id\n" +
				"	LEFT JOIN\n" +
				"		rsc_ts.rsc_ts_ticket_master\n" +
				"	ON\n" +
				"		rsc_ts.rsc_ts_ticket_rate_master.ticket_id = rsc_ts.rsc_ts_ticket_master.id\n" +
				"	LEFT JOIN\n" +
				"		rsc_ts.rsc_ts_visitor_type_master\n" +
				"	ON\n" +
				"		rsc_ts.rsc_ts_ticket_rate_master.visitor_id = rsc_ts.rsc_ts_visitor_type_master.id\n" +
				"	WHERE\n" +
				"		rsc_ts.rsc_ts_ticket_rate_master.bill_type = 'TICKET'\n" +
				"		AND\n" +
				"		rsc_ts.rsc_ts_ticket_bill.generated_at BETWEEN :startDateTime AND :endDateTime \n" +
				"	ORDER BY\n" +
				"		serial_no ASC, rsc_ts.rsc_ts_ticket_bill.ticket_serial DESC\n" +
				"	)\n" +
				"SELECT id AS Id, serial_no AS SerialNo, ticket_id AS TicketId, ticket_name AS TicketName, visitor_id AS VisitorId, visitor_name AS VisitorName, total_sum AS TotalSum, persons AS Persons, price AS Price, ticket_serial AS TicketSerial, cancelled_status AS CancelledStatus FROM report_table", nativeQuery = true)
	public List<TicketReportTable> getTicketsReportTableAtDateTime(Timestamp startDateTime, Timestamp endDateTime);

	// Ticket Daily Report
	@Query(value = "WITH report AS (\n"
			+ "	WITH rate AS (\n"
			+ "		SELECT\n"
			+ "			rate.id AS rate_master_id, rate.bill_type, rate.is_active, rate.price, rate.parking_det_id, NULL AS parking_det_name, rate.ticket_id, ticket.ticket_name, rate.visitor_id, visitor.visitor_name, visitor.group_type\n"
			+ "		FROM\n"
			+ "			rsc_ts.rsc_ts_ticket_rate_master rate, rsc_ts.rsc_ts_ticket_master ticket, rsc_ts.rsc_ts_visitor_type_master visitor\n"
			+ "		WHERE\n"
			+ "			rate.bill_type = 'TICKET' AND rate.ticket_id = ticket.id AND rate.visitor_id = visitor.id\n"
			+ "		UNION\n"
			+ "		SELECT"
			+ "			rate.id As rate_master_id, rate.bill_type, rate.is_active, rate.price, rate.parking_det_id, parking.name AS parking_det_name, rate.ticket_id, NULL, rate.visitor_id, NULL, NULL\n"
			+ "		FROM\n"
			+ "			rsc_ts.rsc_ts_ticket_rate_master rate, rsc_ts.rsc_ts_parking_details parking\n"
			+ "		WHERE\n"
			+ "			rate.bill_type = 'PARKING'\n AND rate.parking_det_id = parking.id"
			+ "	)\n"
			+ "	SELECT\n"
			+ "		DENSE_RANK() OVER (ORDER BY DATE(receipt.generated_at)) AS date_serial, DATE(receipt.generated_at) AS bill_date,\n"
			+ "		bill.id, bill.total_sum, bill.bill_id,\n"
			+ "		receipt.persons, receipt.cancelled_status, receipt.ticket_serial, receipt.total_bill, receipt.generated_by,\n"
			+ "		rate.*\n"
			+ "	FROM rsc_ts.rsc_ts_ticket_bill_rows bill\r\n"
			+ "	INNER JOIN rsc_ts.rsc_ts_ticket_bill receipt ON bill.bill_id = receipt.id\n"
			+ "	LEFT JOIN rate ON bill.rate_master_id = rate.rate_master_id\n"
			+ "	)\n"
			+ "	SELECT report.* FROM report WHERE YEAR(report.bill_date) = :yearSearch", nativeQuery = true)
	public List<TicketDailyReport> getDailyReportDetails(Short yearSearch);

	@Query(value = "SELECT bill FROM TicketBillRow bill WHERE bill.rate.billType = \'TICKET\'", nativeQuery = false)
	public List<TicketBillRow> getTicketBillRows();

	@Query(value = "SELECT bill.generatedTicket.ticketSerial FROM TicketBillRow bill WHERE bill.rate.billType = \'TICKET\' ORDER BY bill.generatedTicket.ticketSerial DESC", nativeQuery = false)
	public List<BigInteger> getTicketsSerialDesc();

	@Query(value = "SELECT bill.generatedTicket.ticketSerial FROM TicketBillRow bill WHERE bill.rate.billType = \'TICKET\' AND bill.generatedTicket.generatedAt BETWEEN :startDateTime AND :endDateTime ORDER BY bill.generatedTicket.ticketSerial DESC", nativeQuery = false)
	public List<BigInteger> getTicketsSerialAtDateTimeDesc(Timestamp startDateTime, Timestamp endDateTime);

	@Query(value = "SELECT bill FROM TicketBillRow bill WHERE bill.rate.billType = \'TICKET\' AND bill.generatedTicket.cancelledStatus = :cancelledStatus ORDER BY bill.generatedTicket.ticketSerial DESC", nativeQuery = false)
	public List<TicketBillRow> getCancelledStatusDesc(boolean cancelledStatus);

	@Query(value = "SELECT bill FROM TicketBillRow bill WHERE bill.rate.billType = \'TICKET\' AND bill.generatedTicket.generatedAt BETWEEN :startDateTime AND :endDateTime AND bill.generatedTicket.cancelledStatus = :cancelledStatus ORDER BY bill.generatedTicket.ticketSerial DESC", nativeQuery = false)
	public List<TicketBillRow> getCancelledStatusAtDateTimeDesc(Timestamp startDateTime, Timestamp endDateTime, boolean cancelledStatus);
}
