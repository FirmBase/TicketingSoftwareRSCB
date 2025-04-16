package com.rsc.bhopal.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.rsc.bhopal.dtos.TicketDetailsDTOByGroup;
import com.rsc.bhopal.entity.TicketDetails;
import com.rsc.bhopal.entity.VisitorsType;

@Repository
public interface TicketDetailsRepository extends JpaRepository<TicketDetails, Long> {
	List<TicketDetails> findByIsActive(Boolean isActive);


	@Query(name="GET_PRICE_OF_COMBO_TICKETS",nativeQuery = false,
	value=" select new com.rsc.bhopal.dtos.TicketDetailsDTOByGroup(ticket.id, ticket.name,"+
	"(SELECT rate.price from TicketsRatesMaster rate where rate.ticketType.id = ticket.id and rate.visitorsType.id=:groupId and  rate.isActive= true))"+
	"from TicketDetails ticket")
	List<TicketDetailsDTOByGroup> getAllTicketsByGroup(long groupId);
}
