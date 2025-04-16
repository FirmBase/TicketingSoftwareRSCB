package com.rsc.bhopal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.rsc.bhopal.dtos.TicketSelectorDTO;
import com.rsc.bhopal.entity.TicketDetails;
import com.rsc.bhopal.repos.TicketDetailsRepository;
import com.rsc.bhopal.service.BillCalculatorService;
import com.rsc.bhopal.service.TicketDetailsService;



@SpringBootTest
class TicktingSoftwareRscbApplicationTests {

	@Autowired
	TicketDetailsService ticketDetails;
	
	@Autowired
	BillCalculatorService billCalculatorService;
	
	@MockBean
	TicketDetailsRepository ticketRepo;
	
	@Test
	public void getTicketDetails() {
		// TicketDetails t1 = new TicketDetails();
		// t1.setId(1l);
		// t1.setName("Dummy 1");
		
		// TicketDetails t2 = new TicketDetails();
		// t2.setId(2l);
		// t2.setName("Dummy 2");
		
		// when(ticketRepo.findAll()).thenReturn(Stream.of(t1,t2).toList());
		
		// assertEquals(2, ticketDetails.getAllTickets().size());
		
	}
	
	
}
