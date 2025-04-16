package com.rsc.bhopal.repos;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.rsc.bhopal.entity.TicketSummary;

public interface TicketSummaryRepository extends JpaRepository<TicketSummary, Long> {
	/*
	@Query(value="SELECT \n" + 
				"    (COUNT(rows_.id)) COUNT,\n" + 
				"    ticket_name TICKET,\n" + 
				"    visitor_name GROUP_, \n" + 
				"     SUM(price) TOTAL\n" + 
				"FROM \n" + 
				"    rsc_ts.rsc_ts_ticket_bill_rows rows_\n" + 
				"LEFT JOIN \n" + 
				"    rsc_ts.rsc_ts_ticket_rate_master ON rate_master_id = rsc_ts.rsc_ts_ticket_rate_master.id\n" + 
				"LEFT JOIN \n" + 
				"    rsc_ts.rsc_ts_ticket_master ON ticket_id = rsc_ts.rsc_ts_ticket_master.id\n" + 
				"LEFT JOIN \n" + 
				"    rsc_ts.rsc_ts_visitor_type_master ON visitor_id = rsc_ts.rsc_ts_visitor_type_master.id\n" + 
				"WHERE bill_type <> 'PARKING'\n" + 
				"#AND rows_.bill_id in (select id from rsc_ts_ticket_bill where   DATE_FORMAT(generated_at,'%Y-%m-%d')  = DATE_FORMAT(sysdate(),'%Y-%m-%d'))\n" + 
				"AND rows_.bill_id in (select id from rsc_ts_ticket_bill where   \n" + 
				"#DATE_FORMAT(generated_at,'%Y-%m-%d')  between  (select CAST(DATE_FORMAT(NOW() ,'%Y-%m-01') as DATE)) and  DATE_FORMAT(sysdate(),'%Y-%m-%d'))\n" + 
				"DATE_FORMAT(generated_at,'%Y-%m-%d')  between  (select CAST(DATE_FORMAT(:startDate,'%Y-%m-%d') as DATE)) and  DATE_FORMAT(:endDate,'%Y-%m-%d'))\n" + 
				"GROUP BY\n" + 
				"ticket_name\n" + 
				",visitor_name\n" + 
				"ORDER BY \n" + 
				" TOTAL DESC, ticket_name ASC, visitor_name ASC", nativeQuery = true)
	public List<com.rsc.bhopal.projections.TicketSummary> getTicketSummaryCountByTicketsAndGroups(String startDate,String endDate);
	*/

	@Query(value = "WITH final_table AS (\n" +
				"	WITH filter_table AS (\n" +
				"		SELECT\n" +
				"			COUNT(paid_bill_row.rate_master_id) AS rate_master_count, SUM(paid_bill_row.total_sum) AS total, paid_bill_row.rate_master_id\n" +
				"		FROM\n" +
				"			(SELECT\n" +
				"				rsc_ts.rsc_ts_ticket_bill_rows.id, rsc_ts.rsc_ts_ticket_bill_rows.total_sum, rsc_ts.rsc_ts_ticket_bill_rows.bill_id, rsc_ts.rsc_ts_ticket_bill_rows.rate_master_id, rsc_ts.rsc_ts_ticket_bill.generated_at\n" +
				"			FROM\n" +
				"				rsc_ts.rsc_ts_ticket_bill_rows\n" +
				"			LEFT JOIN\n" +
				"				rsc_ts.rsc_ts_ticket_bill\n" +
				"			ON rsc_ts.rsc_ts_ticket_bill_rows.bill_id = rsc_ts.rsc_ts_ticket_bill.id\n" +
				"			WHERE\n" +
				"				DATE_FORMAT(generated_at, '%Y-%m-%d') BETWEEN CAST(DATE_FORMAT(:startDate,'%Y-%m-%d') AS DATE) AND CAST(DATE_FORMAT(:endDate, '%Y-%m-%d') AS DATE)\n" +
				"			) paid_bill_row\n" +
				"		GROUP BY\n" +
				"			paid_bill_row.rate_master_id\n" +
				"	) SELECT\n" +
				"		filter_table.rate_master_count, filter_table.total, filter_table.rate_master_id, all_master.price, IFNULL(filter_table.total / all_master.price, filter_table.rate_master_count) AS ticket_count, all_master.ticket_name, all_master.visitor_name\n" +
				"	FROM\n" +
				"		filter_table\n" +
				"	LEFT JOIN\n" +
				"		(SELECT\n" +
				"			rsc_ts.rsc_ts_ticket_rate_master.id, rsc_ts.rsc_ts_ticket_rate_master.bill_type, rsc_ts.rsc_ts_ticket_rate_master.price, rsc_ts.rsc_ts_ticket_master.ticket_name, rsc_ts.rsc_ts_visitor_type_master.visitor_name\n" +
				"		FROM\n" +
				"			rsc_ts.rsc_ts_ticket_rate_master\n" +
				"		LEFT JOIN\n" +
				"			rsc_ts.rsc_ts_ticket_master ON rsc_ts.rsc_ts_ticket_rate_master.ticket_id = rsc_ts.rsc_ts_ticket_master.id\n" +
				"		LEFT JOIN\n" +
				"			rsc_ts.rsc_ts_visitor_type_master ON rsc_ts.rsc_ts_ticket_rate_master.visitor_id = rsc_ts.rsc_ts_visitor_type_master.id\n" +
				"		) all_master ON filter_table.rate_master_id = all_master.id\n" +
				"	WHERE bill_type = 'TICKET'\n" +
				") SELECT\n" +
				"	final_table.ticket_count AS COUNT, final_table.ticket_name AS TICKET, final_table.visitor_name AS GROUP_, final_table.price AS TOTAL\n" +
				"FROM\n" +
				"	final_table;", nativeQuery = true)
	public List<com.rsc.bhopal.projections.TicketSummary> getTicketSummaryCountByTicketsAndGroups(String startDate, String endDate);

/*
	@Query(name = "GET_BILL_SUMMARY_COUNT",
		value = "SELECT COUNT(bill.id) AS co, rate.ticketType.name, rate.visitorsType.name, SUM(rate.price) FROM TicketSummary bill JOIN bill.ticketRatesMaster rate WHERE rate.billType <> 'PARKING' GROUP BY rate.ticketType.name, rate.visitorsType.name",
		nativeQuery = true)
	public List<com.rsc.bhopal.projections.TicketSummary> getAllBillSummary(String startDate, String endDate);
*/
}
