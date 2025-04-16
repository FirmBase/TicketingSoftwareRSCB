package com.rsc.bhopal.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rsc.bhopal.annotations.RSCLog;
import com.rsc.bhopal.dtos.TicketDetailsDTO;
import com.rsc.bhopal.dtos.TicketDetailsDTOByGroup;
import com.rsc.bhopal.entity.RSCUser;
import com.rsc.bhopal.entity.TicketDetails;
import com.rsc.bhopal.repos.TicketDetailsRepository;

import jakarta.transaction.Transactional;

@Service
public class TicketDetailsService {

	@Autowired
	private TicketDetailsRepository ticketRepo;

	@Autowired
	private RSCUserDetailsService userDetailsService;

	public List<TicketDetailsDTOByGroup> getAllTicketsByGroup(long groupId){
		return ticketRepo.getAllTicketsByGroup(groupId);
	}

	public Optional<TicketDetails> getTicketsById(long ticketId) {
		Optional<TicketDetails> ticket = ticketRepo.findById(ticketId);		
		return ticket;
	}

	@RSCLog(desc="Ticket Add")
	public void addTicket(String name,String username){	
		TicketDetails ticket = new TicketDetails();
		ticket.setName(name);
		ticket.setIsActive(true);
		ticket.setAddedAt(new Date());
		RSCUser user = userDetailsService.getUserByUsername(username);		
		ticket.setAddedBy(user);
		ticketRepo.save(ticket);
	}

	@RSCLog(desc="Ticket Status Change")
	public void changeTicketStatus(Long ticketId) {
		Optional<TicketDetails> ticket =	ticketRepo.findById(ticketId);			
		ticket.get().setIsActive(!ticket.get().getIsActive());
		ticketRepo.save(ticket.get());
	}

	@Transactional
	public void addTickets(){		
		List<TicketDetails> ticketDetails = Arrays.asList
				 (new TicketDetails("Museum"),
				new TicketDetails("Taramandal"),
				new TicketDetails("3D Ticket"),
				new TicketDetails("Science Centre + Taramandal"),
				new TicketDetails("Science Centre + 3D"),
				new TicketDetails("Science Centre + Taramandal	+ 3D"),
				new TicketDetails("SDL/Film Show")); 
		ticketDetails.forEach(System.out::println);
		ticketRepo.saveAll(ticketDetails);			
	}

	public List<TicketDetailsDTO> getAllActiveTickets(){
		List<TicketDetails> tickets = ticketRepo.findByIsActive(true);
		List<TicketDetailsDTO> ticketsDTOs = new ArrayList<TicketDetailsDTO>();

		for(TicketDetails ticket : tickets ) {
			TicketDetailsDTO ticketDTO = new TicketDetailsDTO();
			BeanUtils.copyProperties(ticket, ticketDTO);			
			ticketDTO.setAddedBy(ticket.getAddedBy().getName());
			ticketsDTOs.add(ticketDTO);
		}		
		return ticketsDTOs;
	}

	public List<TicketDetailsDTO> getAllTickets(){
		List<TicketDetails> tickets = ticketRepo.findAll();
		List<TicketDetailsDTO> ticketsDTOs = new ArrayList<TicketDetailsDTO>();

		for(TicketDetails ticket : tickets ) {
			TicketDetailsDTO ticketDTO = new TicketDetailsDTO();
			BeanUtils.copyProperties(ticket, ticketDTO);			
			ticketDTO.setAddedBy(ticket.getAddedBy().getName());
			ticketsDTOs.add(ticketDTO);
		}		
		return ticketsDTOs;
	}
}
