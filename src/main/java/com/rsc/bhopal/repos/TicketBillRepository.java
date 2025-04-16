package com.rsc.bhopal.repos;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.rsc.bhopal.entity.TicketBill;

@Repository
public interface TicketBillRepository extends JpaRepository<TicketBill, Long>, PagingAndSortingRepository<TicketBill, Long> {

    // repository.findWithPageable(new PageRequest(0, 10, Direction.DESC, "id"));
	@Query(value="from TicketBill")
	List<TicketBill> recentRecords(Pageable pageable);

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
				"  ticket_name ,visitor_name", nativeQuery = true)
	List<TicketSummary> getTicketSummary(String startDate,String endDate); 
*/
}
