package com.rsc.bhopal.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rsc.bhopal.repos.TicketSummaryRepository;
import com.rsc.bhopal.dtos.TicketDetailsDTO;
import com.rsc.bhopal.dtos.TicketSummaryDTO;
import com.rsc.bhopal.dtos.TicketsRatesMasterDTO;
import com.rsc.bhopal.dtos.VisitorsTypeDTO;

import java.util.List;

@Service
public class TicketSummaryService {
	@Autowired
	private TicketSummaryRepository ticketSummaryRepository;

	public List<TicketSummaryDTO> getAllTicketSummary() {
		List<TicketSummaryDTO> ticketSummaryDTOs = new java.util.ArrayList<TicketSummaryDTO>();
		ticketSummaryRepository.findAll().forEach(ticketSummary -> {
			TicketSummaryDTO ticketSummaryDTO = new TicketSummaryDTO();
			BeanUtils.copyProperties(ticketSummary, ticketSummaryDTO);

			TicketsRatesMasterDTO ticketsRatesMasterDTO = new TicketsRatesMasterDTO();
			BeanUtils.copyProperties(ticketSummary.getTicketsRatesMaster(), ticketsRatesMasterDTO);
			ticketSummaryDTO.setTicketsRatesMasterDTO(ticketsRatesMasterDTO);

			TicketDetailsDTO ticketDetailsDTO = new TicketDetailsDTO();
			try {
				BeanUtils.copyProperties(ticketSummary.getTicketsRatesMaster().getTicketType(), ticketDetailsDTO);
			}
			catch(IllegalArgumentException ex) {
			}
			ticketSummaryDTO.getTicketsRatesMasterDTO().setTicketType(ticketDetailsDTO);

			VisitorsTypeDTO visitorsTypeDTO = new VisitorsTypeDTO();
			try {
				BeanUtils.copyProperties(ticketSummary.getTicketsRatesMaster().getVisitorsType(), visitorsTypeDTO);
			}
			catch(IllegalArgumentException ex) {
			}
			ticketSummaryDTO.getTicketsRatesMasterDTO().setVisitorsType(visitorsTypeDTO);

			ticketSummaryDTOs.add(ticketSummaryDTO);
		});
		return ticketSummaryDTOs;
	}

	public List<com.rsc.bhopal.projections.TicketSummary> getTicketSummaryCountByTicketsAndGroups(String startDate, String endDate) {
		return ticketSummaryRepository.getTicketSummaryCountByTicketsAndGroups(startDate, endDate);
	}
}
