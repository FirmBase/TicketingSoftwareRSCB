package com.rsc.bhopal.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rsc.bhopal.dtos.BillDescription;
import com.rsc.bhopal.dtos.BillSummarize;
import com.rsc.bhopal.dtos.ParkingBillDescription;
import com.rsc.bhopal.dtos.TicketSelectorDTO;
import com.rsc.bhopal.dtos.TicketsRatesMasterDTO;
import com.rsc.bhopal.dtos.VisitorsTypeDTO;
import com.rsc.bhopal.enums.GroupType;
import com.rsc.bhopal.exception.TicketRateNotMaintainedException;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class BillCalculatorService {

	@Autowired
	private TicketsRatesService ticketsRatesService;

	@Autowired
	private VisitorTypeService visitorTypeService;

	@Autowired
	private TicketDetailsService ticketDetailsService;

	public BillSummarize summarizeBill(final TicketSelectorDTO ticketSelectorDTO) {
		log.debug(ticketSelectorDTO.toString());
		
		BillSummarize billSummarize = new BillSummarize();
		if (ticketSelectorDTO.getFamilyGroup() == 0) {
			if (ticketSelectorDTO.getTickets().size() < 1) {
				throw new TicketRateNotMaintainedException("No selected tickets received by server.");
			}
			billSummarize.setBillDescription(summarizeTicketBill(ticketSelectorDTO));
			billSummarize.setComboCase(false);
		}
		else {
			billSummarize.setComboCase(true);
			billSummarize.setBillDescription(summarizeComboBill(ticketSelectorDTO.getFamilyGroup()));
		}
		// billSummarize.getParkingBillDescription();
		// log.debug(""+ticketSelectorDTO);
		try {
			billSummarize.setParkingBillDescription(getParkingBillDescription(ticketSelectorDTO));
		}
		catch(NullPointerException ex) {
			log.debug("Parking empty, " + ex.getMessage());
		}
		return billSummarize;
	}

	public List<BillDescription> summarizeTicketBill(TicketSelectorDTO ticketSelectorDTO) {
		List<BillDescription> bills = new ArrayList<BillDescription>();

		for (final Long ticketId: ticketSelectorDTO.getTickets()) {
			final TicketsRatesMasterDTO ticketRate = ticketsRatesService.getTicketRateByGroup(ticketId, ticketSelectorDTO.getGroup());

			if(ticketRate==null) {
				 String ticketName = ticketDetailsService.getTicketsById(ticketId).get().getName();
				 throw new TicketRateNotMaintainedException("Ticket Rate is not maintained for "+ticketName);
			}
			
			BillDescription bill = new BillDescription();

			bill.setTicket(ticketRate.getTicketType().getName());
			bill.setGroupName(ticketRate.getVisitorsType().getName());
			bill.setPerson(ticketSelectorDTO.getPersons());
			bill.setPerPersonPrice(ticketRate.getPrice());
			bill.setTotalSum(ticketSelectorDTO.getPersons() * ticketRate.getPrice());
			bills.add(bill);
		}
		return bills;
	}

	public List<BillDescription> summarizeComboBill(long familyGroupId){

		List<BillDescription> bills = new ArrayList<BillDescription>();

		final VisitorsTypeDTO visitorDTO = visitorTypeService.getGeneralVisitorId(familyGroupId);
		final List<Long> tickets = ticketsRatesService.getTicketsByGroup(familyGroupId);
		
		if(tickets==null || tickets.isEmpty()) {
			 throw new TicketRateNotMaintainedException("Tickets not added in Combo Group.");
		}

		for (final Long ticketId: tickets) {
			TicketsRatesMasterDTO ticketRate = ticketsRatesService.getTicketRateByGroup(ticketId, familyGroupId);	
			
			if(ticketRate==null) {
				 throw new TicketRateNotMaintainedException("Ticket Rate is not maintained for Combo Group.");
			}
			BillDescription bill = new BillDescription();

			bill.setTicket(ticketRate.getTicketType().getName());
			bill.setGroupName(ticketRate.getVisitorsType().getName());
			// bill.setPerson(visitorDTO.getMinMembers());
			bill.setPerPersonPrice(ticketRate.getPrice());
			if (visitorDTO.getGroupType() == GroupType.SINGLE) {
				bill.setPerson(visitorDTO.getMinMembers());
				bill.setTotalSum(visitorDTO.getMinMembers() * ticketRate.getPrice());
			}
			else if (visitorDTO.getGroupType() == GroupType.COMBO) {
				bill.setPerson(visitorDTO.getFixedMembers());
				bill.setTotalSum(ticketRate.getPrice());
			}
			bills.add(bill);
		}
		return bills;
	}

	public List<ParkingBillDescription> getParkingBillDescription(TicketSelectorDTO ticketSelectorDTO) {
		List<ParkingBillDescription> billDescList = new ArrayList<ParkingBillDescription>();
		final List<TicketsRatesMasterDTO> ticketsRatesMasterDTOs = ticketsRatesService.getActiveParkingDetails();
		ticketSelectorDTO.getParkings().forEach(parking -> {
			if (parking.getCount() > 0){
				for (TicketsRatesMasterDTO ticketsRatesMasterDTO: ticketsRatesMasterDTOs) {
					if (ticketsRatesMasterDTO.getParkingDetails().getId() == parking.getId()) {
						ParkingBillDescription billDesc = new ParkingBillDescription();
						billDesc.setDesc(ticketsRatesMasterDTO.getParkingDetails().getName());
						billDesc.setCount(parking.getCount());
						billDesc.setPerCharge(ticketsRatesMasterDTO.getPrice());
						billDesc.setSum(ticketsRatesMasterDTO.getPrice() * parking.getCount());
						billDescList.add(billDesc);
					}
				}
			}
		});
		return billDescList;
	}

	public double calculateTotal(TicketSelectorDTO ticketSelectorDTO) {
		double totalAmount = 0;
		for (final Long ticketId: ticketSelectorDTO.getTickets()) {
			TicketsRatesMasterDTO ticketRate = ticketsRatesService.getTicketRateByGroup(ticketId, ticketSelectorDTO.getGroup());
			totalAmount += (ticketRate.getPrice() * ticketSelectorDTO.getPersons());
		}
		return totalAmount;
	}
}
