package com.rsc.bhopal.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ser.std.StdArraySerializers.DoubleArraySerializer;
import com.rsc.bhopal.entity.TicketsRatesMaster;

@Repository
public interface TicketsRatesMasterRepository extends JpaRepository<TicketsRatesMaster, Long> {

	@Query(name="GET_TICKET_RATE_BY_GROUP_AND_TICKET",
			value = "select rate from  TicketsRatesMaster rate where rate.ticketType.id =:ticketid and rate.visitorsType.id=:groupId and isActive=true")
	TicketsRatesMaster findByGroupAndTicketIds(long ticketid, long groupId);
	
	@Query(name="CHECK_IF_RATE_IS_AVAIABLE",
			value = "select count(*) from  TicketsRatesMaster rate where rate.ticketType.id =:ticketid and rate.visitorsType.id=:groupId and isActive=true")
	Integer checkIfRateIsAvaiable(long ticketid,long groupId);
	
	@Query(name="GET_TICKET_RATE_BY_GROUP",
			value = "select rate.ticketType.id  from  TicketsRatesMaster rate where rate.visitorsType.id=:groupId and isActive=true")
	List<Long> getTicketsByGroup(long groupId);
	

	@Query(name="GET_PARKING_TICKET_RATE",
			value = "select rate from  TicketsRatesMaster rate where rate.billType=\'PARKING\' and isActive=true")
	List<TicketsRatesMaster> findActiveParking();

	@Query(name="GET_TICKET_RATE_BY_GROUP_AND_TICKET",
			value = "select rate from  TicketsRatesMaster rate where revisionNo>0 order by revisedAt desc")
	List<TicketsRatesMaster> getRecentRevisions();

	TicketsRatesMaster findByIsActiveAndParkingDetails_Id(boolean isActive,Long parkingId);

	@Query(name = "GET_ACTIVE_VISITOR_TICKETS_BY_COMBO_ID",
		value = "select rate from TicketsRatesMaster rate where visitorsType.id=:visitorsTypeId and isActive=true")
	List<TicketsRatesMaster> getAllActiveRatesOfGroup(long visitorsTypeId);

	@Query(name = "GET_ACTIVE_PARKING_RATE",
		value = "select rate from TicketsRatesMaster rate where rate.parkingDetails.id=:parkingId and isActive=true")
		TicketsRatesMaster getActiveParkingRate(Long parkingId);


}

