package com.rsc.bhopal.service.report;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.rsc.bhopal.dtos.TicketBillRowDTO;
import com.rsc.bhopal.dtos.TicketReportTableDTO;
import com.rsc.bhopal.dtos.report.RSCBReportGroup;
import com.rsc.bhopal.dtos.report.RSCBReportTicket;
import com.rsc.bhopal.enums.GroupType;

public class RSCBReportSummary {
	public void arrangeDataInTable(List<Long> visitorSingleIds, List<Long> ticketComboIds, LinkedHashMap<Long, RSCBReportTicket> tickets, LinkedHashMap<Long, RSCBReportTicket> visitors, List<TicketBillRowDTO> ticketBillRowDTOs, double []gross) {
		HashMap<Long, Integer> visitorSingleIdToIndex = new HashMap<Long, Integer>();
		for (int index = 0; index < visitorSingleIds.size(); ++index) {
			visitorSingleIdToIndex.put(visitorSingleIds.get(index), index);
		}

		for (final TicketBillRowDTO ticketBillRowDTO: ticketBillRowDTOs) {
			// if (ticketBillRowDTO.getTicketsRatesMasterDTO().getBillType() != BillType.TICKET)
				// continue;

			if (ticketBillRowDTO.getTicketsRatesMasterDTO().getVisitorsType().getGroupType() == GroupType.SINGLE) {
				RSCBReportTicket ticket = tickets.get(ticketBillRowDTO.getTicketsRatesMasterDTO().getTicketType().getId());
				if (ticket == null) {
					ticket = new RSCBReportTicket();
					ticket.setTicketId(ticketBillRowDTO.getTicketsRatesMasterDTO().getTicketType().getId());
					ticket.setTicketName(ticketBillRowDTO.getTicketsRatesMasterDTO().getTicketType().getName());
					ticket.setGroup(new HashMap<Long, RSCBReportGroup>());

					RSCBReportGroup group = new RSCBReportGroup();
					group.setGroupId(ticketBillRowDTO.getTicketsRatesMasterDTO().getVisitorsType().getId());
					group.setGroupName(ticketBillRowDTO.getTicketsRatesMasterDTO().getVisitorsType().getName());
					group.setCount(ticketBillRowDTO.getTicketBillDTO().getPersons());
					group.setPrice(ticketBillRowDTO.getTicketsRatesMasterDTO().getPrice());
					group.setPersonCount(ticketBillRowDTO.getTicketBillDTO().getPersons());
					group.setTicketSerial(ticketBillRowDTO.getTicketBillDTO().getBillSummarize().getTicketSerial());
					ticket.getGroup().put(Long.valueOf(visitorSingleIdToIndex.get(ticketBillRowDTO.getTicketsRatesMasterDTO().getVisitorsType().getId())), group);

					ticket.setSubTotal(ticketBillRowDTO.getTicketBillDTO().getPersons() * ticketBillRowDTO.getTicketsRatesMasterDTO().getPrice());

					tickets.put(ticketBillRowDTO.getTicketsRatesMasterDTO().getTicketType().getId(), ticket);

					gross[0] += ticketBillRowDTO.getTicketBillDTO().getCancelledStatus() ? 0 : ticketBillRowDTO.getTicketBillDTO().getPersons() * group.getPrice();
				}
				else {
					RSCBReportGroup groups = ticket.getGroup().get(Long.valueOf(visitorSingleIdToIndex.get(ticketBillRowDTO.getTicketsRatesMasterDTO().getVisitorsType().getId())));
					if (groups == null) {
						RSCBReportGroup group = new RSCBReportGroup();
						group.setGroupId(ticketBillRowDTO.getTicketsRatesMasterDTO().getVisitorsType().getId());
						group.setGroupName(ticketBillRowDTO.getTicketsRatesMasterDTO().getVisitorsType().getName());
						group.setCount(ticketBillRowDTO.getTicketBillDTO().getPersons());
						group.setPrice(ticketBillRowDTO.getTicketsRatesMasterDTO().getPrice());
						group.setPersonCount(ticketBillRowDTO.getTicketBillDTO().getPersons());
						group.setTicketSerial(ticketBillRowDTO.getTicketBillDTO().getBillSummarize().getTicketSerial());

						ticket.getGroup().put(Long.valueOf(visitorSingleIdToIndex.get(ticketBillRowDTO.getTicketsRatesMasterDTO().getVisitorsType().getId())), group);
						ticket.setSubTotal(ticket.getSubTotal() + (ticketBillRowDTO.getTicketBillDTO().getPersons() * ticketBillRowDTO.getTicketsRatesMasterDTO().getPrice()));

						gross[0] += ticketBillRowDTO.getTicketBillDTO().getCancelledStatus() ? 0 : ticketBillRowDTO.getTicketBillDTO().getPersons() * group.getPrice();
					}
					else {
						groups.setCount(groups.getCount() + ticketBillRowDTO.getTicketBillDTO().getPersons());
						ticket.setSubTotal(ticket.getSubTotal() + (ticketBillRowDTO.getTicketBillDTO().getPersons() * ticketBillRowDTO.getTicketsRatesMasterDTO().getPrice()));

						gross[0] += ticketBillRowDTO.getTicketBillDTO().getCancelledStatus() ? 0 : ticketBillRowDTO.getTicketBillDTO().getPersons() * ticketBillRowDTO.getTicketsRatesMasterDTO().getPrice();
					}
				}
			}
			else if (ticketBillRowDTO.getTicketsRatesMasterDTO().getVisitorsType().getGroupType() == GroupType.COMBO) {
				RSCBReportTicket ticket = visitors.get(ticketBillRowDTO.getTicketsRatesMasterDTO().getVisitorsType().getId());
				if (ticket == null) {
					ticket = new RSCBReportTicket();
					ticket.setTicketId(ticketBillRowDTO.getTicketsRatesMasterDTO().getVisitorsType().getId());
					ticket.setTicketName(ticketBillRowDTO.getTicketsRatesMasterDTO().getVisitorsType().getName());
					ticket.setGroup(new HashMap<Long, RSCBReportGroup>());

					RSCBReportGroup group = new RSCBReportGroup();
					group.setGroupId(ticketBillRowDTO.getTicketsRatesMasterDTO().getTicketType().getId());
					group.setGroupName(ticketBillRowDTO.getTicketsRatesMasterDTO().getTicketType().getName());
					group.setCount(ticketBillRowDTO.getTicketBillDTO().getPersons());
					group.setPrice(ticketBillRowDTO.getTicketsRatesMasterDTO().getPrice());
					group.setPersonCount(ticketBillRowDTO.getTicketBillDTO().getPersons());
					group.setTicketSerial(ticketBillRowDTO.getTicketBillDTO().getTicketSerial());

					ticket.setSubTotal(Double.valueOf(ticketBillRowDTO.getTicketsRatesMasterDTO().getPrice()));

					ticket.getGroup().put(ticketBillRowDTO.getTicketBillDTO().getId(), group);
					visitors.put(ticketBillRowDTO.getTicketsRatesMasterDTO().getVisitorsType().getId(), ticket);

					ticket.setTicketCount(1);

					gross[0] += ticketBillRowDTO.getTicketBillDTO().getCancelledStatus() ? 0 : ticketBillRowDTO.getTicketsRatesMasterDTO().getPrice();
				}
				else {
					RSCBReportGroup group = ticket.getGroup().get(ticketBillRowDTO.getTicketBillDTO().getId());
					if (group == null) {
						group = new RSCBReportGroup();
						group.setGroupId(ticketBillRowDTO.getTicketsRatesMasterDTO().getTicketType().getId());
						group.setGroupName(ticketBillRowDTO.getTicketsRatesMasterDTO().getTicketType().getName());
						group.setCount(ticketBillRowDTO.getTicketBillDTO().getPersons());
						group.setPrice(ticketBillRowDTO.getTicketsRatesMasterDTO().getPrice());
						group.setPersonCount(ticketBillRowDTO.getTicketBillDTO().getPersons());
						group.setTicketSerial(ticketBillRowDTO.getTicketBillDTO().getTicketSerial());

						ticket.setSubTotal(ticket.getSubTotal() + ticketBillRowDTO.getTicketsRatesMasterDTO().getPrice());

						ticket.getGroup().put(ticketBillRowDTO.getTicketBillDTO().getId(), group);

						ticket.setTicketCount(ticket.getTicketCount() + 1);

						gross[0] += ticketBillRowDTO.getTicketBillDTO().getCancelledStatus() ? 0 : ticketBillRowDTO.getTicketsRatesMasterDTO().getPrice();
					}
					else {
						ticket.setSubTotal(ticket.getSubTotal() + ticketBillRowDTO.getTicketsRatesMasterDTO().getPrice());
						gross[0] += ticketBillRowDTO.getTicketBillDTO().getCancelledStatus() ? 0 : ticketBillRowDTO.getTicketsRatesMasterDTO().getPrice();
					}
				}
				/*
				RSCBReportTicket ticket = visitors.get(ticketBillRowDTO.getTicketsRatesMasterDTO().getVisitorsType().getId());
				if (ticket == null) {
					ticket = new RSCBReportTicket();
					ticket.setTicketId(ticketBillRowDTO.getTicketsRatesMasterDTO().getVisitorsType().getId());
					ticket.setTicketName(ticketBillRowDTO.getTicketsRatesMasterDTO().getVisitorsType().getName());
					ticket.setGroup(new HashMap<Long, RSCBReportGroup>());

					RSCBReportGroup group = new RSCBReportGroup();
					group.setGroupId(ticketBillRowDTO.getTicketsRatesMasterDTO().getTicketType().getId());
					group.setGroupName(ticketBillRowDTO.getTicketsRatesMasterDTO().getTicketType().getName());
					group.setCount(ticketBillRowDTO.getTicketBillDTO().getPersons());
					group.setPrice(ticketBillRowDTO.getTicketsRatesMasterDTO().getPrice());
					group.setPersonCount(ticketBillRowDTO.getTicketBillDTO().getPersons());
					group.setTicketSerial(ticketBillRowDTO.getTicketBillDTO().getTicketSerial());

					ticket.setSubTotal(Double.valueOf(ticketBillRowDTO.getTicketsRatesMasterDTO().getPrice()));

					ticket.getGroup().put(Long.valueOf(ticketComboIdToIndex.get(visitorId)), group);
					visitors.put(ticketBillRowDTO.getTicketsRatesMasterDTO().getVisitorsType().getId(), ticket);

					gross[0] += ticketReportTableDTO.getCancelledStatus() ? 0 : ticketBillRowDTO.getTicketsRatesMasterDTO().getPrice();
				}
				else {
					RSCBReportGroup group = ticket.getGroup().get(Long.valueOf(ticketComboIdToIndex.get(visitorId)));
					if (group == null) {
						group = new RSCBReportGroup();
						group.setGroupId(ticketBillRowDTO.getTicketsRatesMasterDTO().getTicketType().getId());
						group.setGroupName(ticketBillRowDTO.getTicketsRatesMasterDTO().getTicketType().getName());
						group.setCount(ticketBillRowDTO.getTicketBillDTO().getPersons());
						group.setPrice(ticketBillRowDTO.getTicketsRatesMasterDTO().getPrice());
						group.setPersonCount(ticketBillRowDTO.getTicketBillDTO().getPersons());
						group.setTicketSerial(ticketBillRowDTO.getTicketBillDTO().getTicketSerial());

						ticket.setSubTotal(ticket.getSubTotal() + ticketBillRowDTO.getTicketsRatesMasterDTO().getPrice());

						ticket.getGroup().put(Long.valueOf(ticketComboIdToIndex.get(visitorId)), group);

						gross[0] += ticketReportTableDTO.getCancelledStatus() ? 0 : group.getPrice();
					}
					else {
						ticket.setSubTotal(ticket.getSubTotal() + Double.valueOf(group.getPrice()) + ticketBillRowDTO.getTicketsRatesMasterDTO().getPrice());
						gross[0] += ticketReportTableDTO.getCancelledStatus() ? 0 : group.getPrice();
					}
				}
				*/
			}
		}

		// Make object with zero values that were not present
		for (HashMap.Entry<Long, RSCBReportTicket> ticket: tickets.entrySet()) {
			for (HashMap.Entry<Long, Integer> mappedIndex: visitorSingleIdToIndex.entrySet()) {
				if (ticket.getValue().getGroup().get(Long.valueOf(mappedIndex.getValue())) == null) {
					RSCBReportGroup group = new RSCBReportGroup();
					group.setGroupName("");
					group.setCount(0);
					group.setPrice(0f);
					group.setPersonCount(0);
					ticket.getValue().getGroup().put(Long.valueOf(mappedIndex.getValue()), group);
				}
			}
		}
	}

	public LinkedHashMap<Long, RSCBReportTicket[]> arrangeTableForSingleTickets(List<Long> visitorIds, List<TicketReportTableDTO> ticketReportTableDTOs, double []gross) {
		HashMap<Long, Integer> visitorIdToIndex = new HashMap<Long, Integer>();
		for (int index = 0; index < visitorIds.size(); ++index) {
			visitorIdToIndex.put(visitorIds.get(index), index);
		}

		// final short TOTAL_TICKET = 0;
		// final short ISSUED_TICKET = 1;
		// final short CANCELLED_TICKET = 2;
		final short CANCELLED_TICKET = 0;
		final short ISSUED_TICKET = 1;
		final short TOTAL_TICKET = 2;

		LinkedHashMap<Long, RSCBReportTicket[]> row = new LinkedHashMap<Long, RSCBReportTicket[]>();

		for (final TicketReportTableDTO ticketReportTableDTO: ticketReportTableDTOs) {

			RSCBReportTicket[] tickets = row.get(ticketReportTableDTO.getTicketId());
			if (tickets == null) {
				tickets = new RSCBReportTicket[3];
				tickets[TOTAL_TICKET] = new RSCBReportTicket();
				tickets[TOTAL_TICKET].setTicketId(ticketReportTableDTO.getTicketId());
				tickets[TOTAL_TICKET].setTicketName(ticketReportTableDTO.getTicketName());
				tickets[TOTAL_TICKET].setGroup(new HashMap<Long, RSCBReportGroup>());

				RSCBReportGroup group = new RSCBReportGroup();
				group.setGroupId(ticketReportTableDTO.getVisitorId());
				group.setGroupName(ticketReportTableDTO.getVisitorName());
				group.setCount(ticketReportTableDTO.getPersons());
				group.setPrice(ticketReportTableDTO.getPrice());
				group.setPersonCount(ticketReportTableDTO.getPersons());
				group.setTicketSerial(ticketReportTableDTO.getTicketSerial());
				tickets[TOTAL_TICKET].getGroup().put(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())), group);

				tickets[TOTAL_TICKET].setSubTotal(ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice());

				row.put(ticketReportTableDTO.getTicketId(), tickets);

				gross[0] += ticketReportTableDTO.getCancelledStatus() ? 0 : ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice();

				if (ticketReportTableDTO.getCancelledStatus()) {
					// tickets[CANCELLED_TICKET] = tickets[TOTAL_TICKET];
					tickets[CANCELLED_TICKET] = new RSCBReportTicket();
					tickets[CANCELLED_TICKET].setTicketId(ticketReportTableDTO.getTicketId());
					tickets[CANCELLED_TICKET].setTicketName(ticketReportTableDTO.getTicketName());
					tickets[CANCELLED_TICKET].setGroup(new HashMap<Long, RSCBReportGroup>());

					RSCBReportGroup cancelledTicketGroup = new RSCBReportGroup();
					cancelledTicketGroup.setGroupId(ticketReportTableDTO.getVisitorId());
					cancelledTicketGroup.setGroupName(ticketReportTableDTO.getVisitorName());
					cancelledTicketGroup.setCount(ticketReportTableDTO.getPersons());
					cancelledTicketGroup.setPrice(ticketReportTableDTO.getPrice());
					cancelledTicketGroup.setPersonCount(ticketReportTableDTO.getPersons());
					cancelledTicketGroup.setTicketSerial(ticketReportTableDTO.getTicketSerial());

					tickets[CANCELLED_TICKET].getGroup().put(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())), cancelledTicketGroup);

					tickets[CANCELLED_TICKET].setSubTotal(ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice());

					tickets[ISSUED_TICKET] = null;
				}
				else {
					// tickets[ISSUED_TICKET] = tickets[TOTAL_TICKET];
					tickets[ISSUED_TICKET] = new RSCBReportTicket();
					tickets[ISSUED_TICKET].setTicketId(ticketReportTableDTO.getTicketId());
					tickets[ISSUED_TICKET].setTicketName(ticketReportTableDTO.getTicketName());
					tickets[ISSUED_TICKET].setGroup(new HashMap<Long, RSCBReportGroup>());

					RSCBReportGroup issuedTicketGroup = new RSCBReportGroup();
					issuedTicketGroup.setGroupId(ticketReportTableDTO.getVisitorId());
					issuedTicketGroup.setGroupName(ticketReportTableDTO.getVisitorName());
					issuedTicketGroup.setCount(ticketReportTableDTO.getPersons());
					issuedTicketGroup.setPrice(ticketReportTableDTO.getPrice());
					issuedTicketGroup.setPersonCount(ticketReportTableDTO.getPersons());
					issuedTicketGroup.setTicketSerial(ticketReportTableDTO.getTicketSerial());

					tickets[ISSUED_TICKET].getGroup().put(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())), issuedTicketGroup);

					tickets[ISSUED_TICKET].setSubTotal(ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice());

					tickets[CANCELLED_TICKET] = null;
				}
			}
			else {
				RSCBReportGroup groups = tickets[TOTAL_TICKET].getGroup().get(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())));
				if (groups == null) {
					groups = new RSCBReportGroup();
					groups.setGroupId(ticketReportTableDTO.getTicketId());
					groups.setGroupName(ticketReportTableDTO.getVisitorName());
					groups.setCount(ticketReportTableDTO.getPersons());
					groups.setPrice(ticketReportTableDTO.getPrice());
					groups.setPersonCount(ticketReportTableDTO.getPersons());
					groups.setTicketSerial(ticketReportTableDTO.getTicketSerial());

					tickets[TOTAL_TICKET].getGroup().put(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())), groups);
					tickets[TOTAL_TICKET].setSubTotal(tickets[TOTAL_TICKET].getSubTotal() + (ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice()));

					gross[0] += ticketReportTableDTO.getCancelledStatus() ? 0 : ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice();
				}
				else {
					groups.setCount(groups.getCount() + ticketReportTableDTO.getPersons());
					tickets[TOTAL_TICKET].setSubTotal(tickets[TOTAL_TICKET].getSubTotal() + (ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice()));

					gross[0] += ticketReportTableDTO.getCancelledStatus() ? 0 : ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice();
				}

				if (ticketReportTableDTO.getCancelledStatus()) {
					if (tickets[CANCELLED_TICKET] == null) {
						tickets[CANCELLED_TICKET] = new RSCBReportTicket();
						tickets[CANCELLED_TICKET].setTicketId(ticketReportTableDTO.getTicketId());
						tickets[CANCELLED_TICKET].setTicketName(ticketReportTableDTO.getTicketName());
						tickets[CANCELLED_TICKET].setGroup(new HashMap<Long, RSCBReportGroup>());

						RSCBReportGroup group = new RSCBReportGroup();
						group.setGroupId(ticketReportTableDTO.getVisitorId());
						group.setGroupName(ticketReportTableDTO.getVisitorName());
						group.setCount(ticketReportTableDTO.getPersons());
						group.setPrice(ticketReportTableDTO.getPrice());
						group.setPersonCount(ticketReportTableDTO.getPersons());
						group.setTicketSerial(ticketReportTableDTO.getTicketSerial());
						tickets[CANCELLED_TICKET].getGroup().put(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())), group);

						tickets[CANCELLED_TICKET].setSubTotal(ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice());

						// row.put(ticketReportTableDTO.getTicketId(), tickets);

						// grandTotal += ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice();
					}
					else {
						RSCBReportGroup cancelledTicketGroup = tickets[CANCELLED_TICKET].getGroup().get(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())));
						if (cancelledTicketGroup == null) {
							cancelledTicketGroup = new RSCBReportGroup();
							cancelledTicketGroup.setGroupId(ticketReportTableDTO.getTicketId());
							cancelledTicketGroup.setGroupName(ticketReportTableDTO.getVisitorName());
							cancelledTicketGroup.setCount(ticketReportTableDTO.getPersons());
							cancelledTicketGroup.setPrice(ticketReportTableDTO.getPrice());
							cancelledTicketGroup.setPersonCount(ticketReportTableDTO.getPersons());
							cancelledTicketGroup.setTicketSerial(ticketReportTableDTO.getTicketSerial());
		
							tickets[CANCELLED_TICKET].getGroup().put(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())), cancelledTicketGroup);
						}
						else {
							cancelledTicketGroup.setCount(cancelledTicketGroup.getCount() + ticketReportTableDTO.getPersons());
							tickets[CANCELLED_TICKET].setSubTotal(tickets[CANCELLED_TICKET].getSubTotal() + (ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice()));
						}
					}
				}
				else {
					if (tickets[ISSUED_TICKET] == null) {
						tickets[ISSUED_TICKET] = new RSCBReportTicket();
						tickets[ISSUED_TICKET].setTicketId(ticketReportTableDTO.getTicketId());
						tickets[ISSUED_TICKET].setTicketName(ticketReportTableDTO.getTicketName());
						tickets[ISSUED_TICKET].setGroup(new HashMap<Long, RSCBReportGroup>());

						RSCBReportGroup group = new RSCBReportGroup();
						group.setGroupId(ticketReportTableDTO.getVisitorId());
						group.setGroupName(ticketReportTableDTO.getVisitorName());
						group.setCount(ticketReportTableDTO.getPersons());
						group.setPrice(ticketReportTableDTO.getPrice());
						group.setPersonCount(ticketReportTableDTO.getPersons());
						group.setTicketSerial(ticketReportTableDTO.getTicketSerial());
						tickets[ISSUED_TICKET].getGroup().put(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())), group);

						tickets[ISSUED_TICKET].setSubTotal(ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice());

						// row.put(ticketReportTableDTO.getTicketId(), tickets);

						// grandTotal += ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice();
					}
					else {
						RSCBReportGroup issuededTicketGroup = tickets[ISSUED_TICKET].getGroup().get(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())));
						if (issuededTicketGroup == null) {
							issuededTicketGroup = new RSCBReportGroup();
							issuededTicketGroup.setGroupId(ticketReportTableDTO.getTicketId());
							issuededTicketGroup.setGroupName(ticketReportTableDTO.getVisitorName());
							issuededTicketGroup.setCount(ticketReportTableDTO.getPersons());
							issuededTicketGroup.setPrice(ticketReportTableDTO.getPrice());
							issuededTicketGroup.setPersonCount(ticketReportTableDTO.getPersons());
							issuededTicketGroup.setTicketSerial(ticketReportTableDTO.getTicketSerial());
		
							tickets[ISSUED_TICKET].getGroup().put(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())), issuededTicketGroup);
						}
						else {
							issuededTicketGroup.setCount(issuededTicketGroup.getCount() + ticketReportTableDTO.getPersons());
							tickets[ISSUED_TICKET].setSubTotal(tickets[ISSUED_TICKET].getSubTotal() + (ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice()));
						}
					}
				}
			}
		}

		// Make object with zero values that were not present
		for (HashMap.Entry<Long, RSCBReportTicket[]> element: row.entrySet()) {
			for (short index = 0; index < element.getValue().length; ++index) {
				for (HashMap.Entry<Long, Integer> mappedIndex: visitorIdToIndex.entrySet()) {
					if (element.getValue()[index] == null) {
						RSCBReportTicket ticket = new RSCBReportTicket();
						ticket.setTicketId(0L);
						ticket.setTicketName("");
						ticket.setSubTotal(0);
						ticket.setGroup(new HashMap<Long, RSCBReportGroup>());
						element.getValue()[index] = ticket;
					}
					if (element.getValue()[index].getGroup().get(Long.valueOf(mappedIndex.getValue())) == null) {
						RSCBReportGroup group = new RSCBReportGroup();
						group.setGroupName("");
						group.setCount(0);
						/*
						try {
							group.setPrice(ticketsRatesService.getTicketRateByGroup(element.getValue()[index].getTicketId(), mappedIndex.getKey()).getPrice());
						}
						catch(TicketRateNotMaintainedException ex) {
							group.setPrice(0f);
						}
						catch(java.util.NoSuchElementException ex) {
							group.setPrice(0f);
						}
						*/
						group.setPrice(0f);
						group.setPersonCount(0);
						element.getValue()[index].getGroup().put(Long.valueOf(mappedIndex.getValue()), group);
					}
				}
			}
		}
		return row;
	}

	public LinkedHashMap<Long, RSCBReportTicket[]> arrangeTableForComboTickets(List<Long> visitorIds, List<TicketReportTableDTO> ticketReportTableDTOs, double []gross) {
		HashMap<Long, Integer> visitorIdToIndex = new HashMap<Long, Integer>();
		for (int index = 0; index < visitorIds.size(); ++index) {
			visitorIdToIndex.put(visitorIds.get(index), index);
		}

		// final short TOTAL_TICKET = 0;
		// final short ISSUED_TICKET = 1;
		// final short CANCELLED_TICKET = 2;
		final short CANCELLED_TICKET = 0;
		final short ISSUED_TICKET = 1;
		final short TOTAL_TICKET = 2;

		LinkedHashMap<Long, RSCBReportTicket[]> row = new LinkedHashMap<Long, RSCBReportTicket[]>();

		for (final TicketReportTableDTO ticketReportTableDTO: ticketReportTableDTOs) {

			RSCBReportTicket[] tickets = row.get(ticketReportTableDTO.getTicketId());
			if (tickets == null) {
				tickets = new RSCBReportTicket[3];
				tickets[TOTAL_TICKET] = new RSCBReportTicket();
				tickets[TOTAL_TICKET].setTicketId(ticketReportTableDTO.getTicketId());
				tickets[TOTAL_TICKET].setTicketName(ticketReportTableDTO.getTicketName());
				tickets[TOTAL_TICKET].setGroup(new HashMap<Long, RSCBReportGroup>());

				RSCBReportGroup group = new RSCBReportGroup();
				group.setGroupId(ticketReportTableDTO.getVisitorId());
				group.setGroupName(ticketReportTableDTO.getVisitorName());
				group.setCount(ticketReportTableDTO.getPersons());
				group.setPrice(ticketReportTableDTO.getPrice());
				group.setPersonCount(ticketReportTableDTO.getPersons());
				group.setTicketSerial(ticketReportTableDTO.getTicketSerial());
				tickets[TOTAL_TICKET].getGroup().put(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())), group);

				tickets[TOTAL_TICKET].setTicketCount(1);
				tickets[TOTAL_TICKET].setSubTotal(ticketReportTableDTO.getPrice());

				row.put(ticketReportTableDTO.getTicketId(), tickets);

				gross[0] += ticketReportTableDTO.getCancelledStatus() ? 0 : ticketReportTableDTO.getPrice();

				if (ticketReportTableDTO.getCancelledStatus()) {
					// tickets[CANCELLED_TICKET] = tickets[TOTAL_TICKET];
					tickets[CANCELLED_TICKET] = new RSCBReportTicket();
					tickets[CANCELLED_TICKET].setTicketId(ticketReportTableDTO.getTicketId());
					tickets[CANCELLED_TICKET].setTicketName(ticketReportTableDTO.getTicketName());
					tickets[CANCELLED_TICKET].setGroup(new HashMap<Long, RSCBReportGroup>());

					RSCBReportGroup cancelledTicketGroup = new RSCBReportGroup();
					cancelledTicketGroup.setGroupId(ticketReportTableDTO.getVisitorId());
					cancelledTicketGroup.setGroupName(ticketReportTableDTO.getVisitorName());
					cancelledTicketGroup.setCount(ticketReportTableDTO.getPersons());
					cancelledTicketGroup.setPrice(ticketReportTableDTO.getPrice());
					cancelledTicketGroup.setPersonCount(ticketReportTableDTO.getPersons());
					cancelledTicketGroup.setTicketSerial(ticketReportTableDTO.getTicketSerial());

					tickets[CANCELLED_TICKET].getGroup().put(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())), cancelledTicketGroup);

					tickets[CANCELLED_TICKET].setTicketCount(1);
					tickets[CANCELLED_TICKET].setSubTotal(ticketReportTableDTO.getPrice());

					tickets[ISSUED_TICKET] = null;
				}
				else {
					// tickets[ISSUED_TICKET] = tickets[TOTAL_TICKET];
					tickets[ISSUED_TICKET] = new RSCBReportTicket();
					tickets[ISSUED_TICKET].setTicketId(ticketReportTableDTO.getTicketId());
					tickets[ISSUED_TICKET].setTicketName(ticketReportTableDTO.getTicketName());
					tickets[ISSUED_TICKET].setGroup(new HashMap<Long, RSCBReportGroup>());

					RSCBReportGroup issuedTicketGroup = new RSCBReportGroup();
					issuedTicketGroup.setGroupId(ticketReportTableDTO.getVisitorId());
					issuedTicketGroup.setGroupName(ticketReportTableDTO.getVisitorName());
					issuedTicketGroup.setCount(ticketReportTableDTO.getPersons());
					issuedTicketGroup.setPrice(ticketReportTableDTO.getPrice());
					issuedTicketGroup.setPersonCount(ticketReportTableDTO.getPersons());
					issuedTicketGroup.setTicketSerial(ticketReportTableDTO.getTicketSerial());

					tickets[ISSUED_TICKET].getGroup().put(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())), issuedTicketGroup);

					tickets[ISSUED_TICKET].setTicketCount(1);
					tickets[ISSUED_TICKET].setSubTotal(ticketReportTableDTO.getPrice());

					tickets[CANCELLED_TICKET] = null;
				}
			}
			else {
				RSCBReportGroup groups = tickets[TOTAL_TICKET].getGroup().get(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())));
				if (groups == null) {
					groups = new RSCBReportGroup();
					groups.setGroupId(ticketReportTableDTO.getTicketId());
					groups.setGroupName(ticketReportTableDTO.getVisitorName());
					groups.setCount(ticketReportTableDTO.getPersons());
					groups.setPrice(ticketReportTableDTO.getPrice());
					groups.setPersonCount(ticketReportTableDTO.getPersons());
					groups.setTicketSerial(ticketReportTableDTO.getTicketSerial());

					tickets[TOTAL_TICKET].getGroup().put(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())), groups);
					tickets[TOTAL_TICKET].setSubTotal(tickets[TOTAL_TICKET].getSubTotal() + ticketReportTableDTO.getPrice());
					tickets[TOTAL_TICKET].setTicketCount(tickets[TOTAL_TICKET].getTicketCount() + 1);

					gross[0] += ticketReportTableDTO.getCancelledStatus() ? 0 : ticketReportTableDTO.getPrice();
				}
				else {
					groups.setCount(groups.getCount() + ticketReportTableDTO.getPersons());
					tickets[TOTAL_TICKET].setSubTotal(tickets[TOTAL_TICKET].getSubTotal() + ticketReportTableDTO.getPrice());
					tickets[TOTAL_TICKET].setTicketCount(tickets[TOTAL_TICKET].getTicketCount() + 1);

					gross[0] += ticketReportTableDTO.getCancelledStatus() ? 0 : ticketReportTableDTO.getPrice();
				}

				if (ticketReportTableDTO.getCancelledStatus()) {
					if (tickets[CANCELLED_TICKET] == null) {
						tickets[CANCELLED_TICKET] = new RSCBReportTicket();
						tickets[CANCELLED_TICKET].setTicketId(ticketReportTableDTO.getTicketId());
						tickets[CANCELLED_TICKET].setTicketName(ticketReportTableDTO.getTicketName());
						tickets[CANCELLED_TICKET].setGroup(new HashMap<Long, RSCBReportGroup>());

						RSCBReportGroup group = new RSCBReportGroup();
						group.setGroupId(ticketReportTableDTO.getVisitorId());
						group.setGroupName(ticketReportTableDTO.getVisitorName());
						group.setCount(ticketReportTableDTO.getPersons());
						group.setPrice(ticketReportTableDTO.getPrice());
						group.setPersonCount(ticketReportTableDTO.getPersons());
						group.setTicketSerial(ticketReportTableDTO.getTicketSerial());
						tickets[CANCELLED_TICKET].getGroup().put(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())), group);

						tickets[CANCELLED_TICKET].setTicketCount(1);
						tickets[CANCELLED_TICKET].setSubTotal(ticketReportTableDTO.getPrice());

						// row.put(ticketReportTableDTO.getTicketId(), tickets);

						// grandTotal += ticketReportTableDTO.getPrice();
					}
					else {
						RSCBReportGroup cancelledTicketGroup = tickets[CANCELLED_TICKET].getGroup().get(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())));
						if (cancelledTicketGroup == null) {
							cancelledTicketGroup = new RSCBReportGroup();
							cancelledTicketGroup.setGroupId(ticketReportTableDTO.getTicketId());
							cancelledTicketGroup.setGroupName(ticketReportTableDTO.getVisitorName());
							cancelledTicketGroup.setCount(ticketReportTableDTO.getPersons());
							cancelledTicketGroup.setPrice(ticketReportTableDTO.getPrice());
							cancelledTicketGroup.setPersonCount(ticketReportTableDTO.getPersons());
							cancelledTicketGroup.setTicketSerial(ticketReportTableDTO.getTicketSerial());

							tickets[CANCELLED_TICKET].getGroup().put(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())), cancelledTicketGroup);
						}
						else {
							cancelledTicketGroup.setCount(cancelledTicketGroup.getCount() + ticketReportTableDTO.getPersons());
							tickets[CANCELLED_TICKET].setTicketCount(tickets[CANCELLED_TICKET].getTicketCount() + 1);
							tickets[CANCELLED_TICKET].setSubTotal(tickets[CANCELLED_TICKET].getSubTotal() + ticketReportTableDTO.getPrice());
						}
					}
				}
				else {
					if (tickets[ISSUED_TICKET] == null) {
						tickets[ISSUED_TICKET] = new RSCBReportTicket();
						tickets[ISSUED_TICKET].setTicketId(ticketReportTableDTO.getTicketId());
						tickets[ISSUED_TICKET].setTicketName(ticketReportTableDTO.getTicketName());
						tickets[ISSUED_TICKET].setGroup(new HashMap<Long, RSCBReportGroup>());

						RSCBReportGroup group = new RSCBReportGroup();
						group.setGroupId(ticketReportTableDTO.getVisitorId());
						group.setGroupName(ticketReportTableDTO.getVisitorName());
						group.setCount(ticketReportTableDTO.getPersons());
						group.setPrice(ticketReportTableDTO.getPrice());
						group.setPersonCount(ticketReportTableDTO.getPersons());
						group.setTicketSerial(ticketReportTableDTO.getTicketSerial());
						tickets[ISSUED_TICKET].getGroup().put(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())), group);

						tickets[ISSUED_TICKET].setTicketCount(1);
						tickets[ISSUED_TICKET].setSubTotal(ticketReportTableDTO.getPrice());

						// row.put(ticketReportTableDTO.getTicketId(), tickets);

						// grandTotal += ticketReportTableDTO.getPrice();
					}
					else {
						RSCBReportGroup issuededTicketGroup = tickets[ISSUED_TICKET].getGroup().get(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())));
						if (issuededTicketGroup == null) {
							issuededTicketGroup = new RSCBReportGroup();
							issuededTicketGroup.setGroupId(ticketReportTableDTO.getTicketId());
							issuededTicketGroup.setGroupName(ticketReportTableDTO.getVisitorName());
							issuededTicketGroup.setCount(ticketReportTableDTO.getPersons());
							issuededTicketGroup.setPrice(ticketReportTableDTO.getPrice());
							issuededTicketGroup.setPersonCount(ticketReportTableDTO.getPersons());
							issuededTicketGroup.setTicketSerial(ticketReportTableDTO.getTicketSerial());

							tickets[ISSUED_TICKET].getGroup().put(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())), issuededTicketGroup);
						}
						else {
							issuededTicketGroup.setCount(issuededTicketGroup.getCount() + ticketReportTableDTO.getPersons());
							tickets[ISSUED_TICKET].setTicketCount(tickets[ISSUED_TICKET].getTicketCount() + 1);
							tickets[ISSUED_TICKET].setSubTotal(tickets[ISSUED_TICKET].getSubTotal() + ticketReportTableDTO.getPrice());
						}
					}
				}
			}
		}

		// Make object with zero values that were not present
		for (HashMap.Entry<Long, RSCBReportTicket[]> element: row.entrySet()) {
			for (short index = 0; index < element.getValue().length; ++index) {
				if (element.getValue()[index] == null) {
					RSCBReportTicket ticket = new RSCBReportTicket();
					ticket.setTicketId(0L);
					ticket.setTicketName("");
					ticket.setSubTotal(0);
					ticket.setGroup(new HashMap<Long, RSCBReportGroup>());
					element.getValue()[index] = ticket;
				}
			}
		}
		return row;
	}

	public LinkedHashMap<Long, RSCBReportTicket[]> arrangeTableForParkingTickets(List<Long> visitorIds, List<TicketReportTableDTO> ticketReportTableDTOs, double []gross) {
		HashMap<Long, Integer> visitorIdToIndex = new HashMap<Long, Integer>();
		for (int index = 0; index < visitorIds.size(); ++index) {
			visitorIdToIndex.put(visitorIds.get(index), index);
		}

		// final short TOTAL_TICKET = 0;
		// final short ISSUED_TICKET = 1;
		// final short CANCELLED_TICKET = 2;
		final short CANCELLED_TICKET = 0;
		final short ISSUED_TICKET = 1;
		final short TOTAL_TICKET = 2;

		LinkedHashMap<Long, RSCBReportTicket[]> row = new LinkedHashMap<Long, RSCBReportTicket[]>();

		for (final TicketReportTableDTO ticketReportTableDTO: ticketReportTableDTOs) {

			RSCBReportTicket[] tickets = row.get(ticketReportTableDTO.getTicketId());
			if (tickets == null) {
				tickets = new RSCBReportTicket[3];
				tickets[TOTAL_TICKET] = new RSCBReportTicket();
				tickets[TOTAL_TICKET].setTicketId(ticketReportTableDTO.getTicketId());
				tickets[TOTAL_TICKET].setTicketName(ticketReportTableDTO.getTicketName());
				tickets[TOTAL_TICKET].setGroup(new HashMap<Long, RSCBReportGroup>());

				RSCBReportGroup group = new RSCBReportGroup();
				group.setGroupId(ticketReportTableDTO.getVisitorId());
				group.setGroupName(ticketReportTableDTO.getVisitorName());
				group.setCount(ticketReportTableDTO.getPersons());
				group.setPrice(ticketReportTableDTO.getPrice());
				group.setPersonCount(ticketReportTableDTO.getPersons());
				group.setTicketSerial(ticketReportTableDTO.getTicketSerial());
				tickets[TOTAL_TICKET].getGroup().put(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())), group);

				tickets[TOTAL_TICKET].setSubTotal(ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice());

				row.put(ticketReportTableDTO.getTicketId(), tickets);

				gross[0] += ticketReportTableDTO.getCancelledStatus() ? 0 : ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice();

				if (ticketReportTableDTO.getCancelledStatus()) {
					// tickets[CANCELLED_TICKET] = tickets[TOTAL_TICKET];
					tickets[CANCELLED_TICKET] = new RSCBReportTicket();
					tickets[CANCELLED_TICKET].setTicketId(ticketReportTableDTO.getTicketId());
					tickets[CANCELLED_TICKET].setTicketName(ticketReportTableDTO.getTicketName());
					tickets[CANCELLED_TICKET].setGroup(new HashMap<Long, RSCBReportGroup>());

					RSCBReportGroup cancelledTicketGroup = new RSCBReportGroup();
					cancelledTicketGroup.setGroupId(ticketReportTableDTO.getVisitorId());
					cancelledTicketGroup.setGroupName(ticketReportTableDTO.getVisitorName());
					cancelledTicketGroup.setCount(ticketReportTableDTO.getPersons());
					cancelledTicketGroup.setPrice(ticketReportTableDTO.getPrice());
					cancelledTicketGroup.setPersonCount(ticketReportTableDTO.getPersons());
					cancelledTicketGroup.setTicketSerial(ticketReportTableDTO.getTicketSerial());

					tickets[CANCELLED_TICKET].getGroup().put(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())), cancelledTicketGroup);

					tickets[CANCELLED_TICKET].setSubTotal(ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice());

					tickets[ISSUED_TICKET] = null;
				}
				else {
					// tickets[ISSUED_TICKET] = tickets[TOTAL_TICKET];
					tickets[ISSUED_TICKET] = new RSCBReportTicket();
					tickets[ISSUED_TICKET].setTicketId(ticketReportTableDTO.getTicketId());
					tickets[ISSUED_TICKET].setTicketName(ticketReportTableDTO.getTicketName());
					tickets[ISSUED_TICKET].setGroup(new HashMap<Long, RSCBReportGroup>());

					RSCBReportGroup issuedTicketGroup = new RSCBReportGroup();
					issuedTicketGroup.setGroupId(ticketReportTableDTO.getVisitorId());
					issuedTicketGroup.setGroupName(ticketReportTableDTO.getVisitorName());
					issuedTicketGroup.setCount(ticketReportTableDTO.getPersons());
					issuedTicketGroup.setPrice(ticketReportTableDTO.getPrice());
					issuedTicketGroup.setPersonCount(ticketReportTableDTO.getPersons());
					issuedTicketGroup.setTicketSerial(ticketReportTableDTO.getTicketSerial());

					tickets[ISSUED_TICKET].getGroup().put(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())), issuedTicketGroup);

					tickets[ISSUED_TICKET].setSubTotal(ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice());

					tickets[CANCELLED_TICKET] = null;
				}
			}
			else {
				RSCBReportGroup groups = tickets[TOTAL_TICKET].getGroup().get(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())));
				if (groups == null) {
					groups = new RSCBReportGroup();
					groups.setGroupId(ticketReportTableDTO.getTicketId());
					groups.setGroupName(ticketReportTableDTO.getVisitorName());
					groups.setCount(ticketReportTableDTO.getPersons());
					groups.setPrice(ticketReportTableDTO.getPrice());
					groups.setPersonCount(ticketReportTableDTO.getPersons());
					groups.setTicketSerial(ticketReportTableDTO.getTicketSerial());

					tickets[TOTAL_TICKET].getGroup().put(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())), groups);
					tickets[TOTAL_TICKET].setSubTotal(tickets[TOTAL_TICKET].getSubTotal() + (ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice()));

					gross[0] += ticketReportTableDTO.getCancelledStatus() ? 0 : ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice();
				}
				else {
					groups.setCount(groups.getCount() + ticketReportTableDTO.getPersons());
					tickets[TOTAL_TICKET].setSubTotal(tickets[TOTAL_TICKET].getSubTotal() + (ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice()));

					gross[0] += ticketReportTableDTO.getCancelledStatus() ? 0 : ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice();
				}

				if (ticketReportTableDTO.getCancelledStatus()) {
					if (tickets[CANCELLED_TICKET] == null) {
						tickets[CANCELLED_TICKET] = new RSCBReportTicket();
						tickets[CANCELLED_TICKET].setTicketId(ticketReportTableDTO.getTicketId());
						tickets[CANCELLED_TICKET].setTicketName(ticketReportTableDTO.getTicketName());
						tickets[CANCELLED_TICKET].setGroup(new HashMap<Long, RSCBReportGroup>());

						RSCBReportGroup group = new RSCBReportGroup();
						group.setGroupId(ticketReportTableDTO.getVisitorId());
						group.setGroupName(ticketReportTableDTO.getVisitorName());
						group.setCount(ticketReportTableDTO.getPersons());
						group.setPrice(ticketReportTableDTO.getPrice());
						group.setPersonCount(ticketReportTableDTO.getPersons());
						group.setTicketSerial(ticketReportTableDTO.getTicketSerial());
						tickets[CANCELLED_TICKET].getGroup().put(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())), group);

						tickets[CANCELLED_TICKET].setSubTotal(ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice());
					}
					else {
						RSCBReportGroup cancelledTicketGroup = tickets[CANCELLED_TICKET].getGroup().get(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())));
						if (cancelledTicketGroup == null) {
							cancelledTicketGroup = new RSCBReportGroup();
							cancelledTicketGroup.setGroupId(ticketReportTableDTO.getTicketId());
							cancelledTicketGroup.setGroupName(ticketReportTableDTO.getVisitorName());
							cancelledTicketGroup.setCount(ticketReportTableDTO.getPersons());
							cancelledTicketGroup.setPrice(ticketReportTableDTO.getPrice());
							cancelledTicketGroup.setPersonCount(ticketReportTableDTO.getPersons());
							cancelledTicketGroup.setTicketSerial(ticketReportTableDTO.getTicketSerial());
		
							tickets[CANCELLED_TICKET].getGroup().put(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())), cancelledTicketGroup);
						}
						else {
							cancelledTicketGroup.setCount(cancelledTicketGroup.getCount() + ticketReportTableDTO.getPersons());
							tickets[CANCELLED_TICKET].setSubTotal(tickets[CANCELLED_TICKET].getSubTotal() + (ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice()));
						}
					}
				}
				else {
					if (tickets[ISSUED_TICKET] == null) {
						tickets[ISSUED_TICKET] = new RSCBReportTicket();
						tickets[ISSUED_TICKET].setTicketId(ticketReportTableDTO.getTicketId());
						tickets[ISSUED_TICKET].setTicketName(ticketReportTableDTO.getTicketName());
						tickets[ISSUED_TICKET].setGroup(new HashMap<Long, RSCBReportGroup>());

						RSCBReportGroup group = new RSCBReportGroup();
						group.setGroupId(ticketReportTableDTO.getVisitorId());
						group.setGroupName(ticketReportTableDTO.getVisitorName());
						group.setCount(ticketReportTableDTO.getPersons());
						group.setPrice(ticketReportTableDTO.getPrice());
						group.setPersonCount(ticketReportTableDTO.getPersons());
						group.setTicketSerial(ticketReportTableDTO.getTicketSerial());
						tickets[ISSUED_TICKET].getGroup().put(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())), group);

						tickets[ISSUED_TICKET].setSubTotal(ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice());
					}
					else {
						RSCBReportGroup issuededTicketGroup = tickets[ISSUED_TICKET].getGroup().get(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())));
						if (issuededTicketGroup == null) {
							issuededTicketGroup = new RSCBReportGroup();
							issuededTicketGroup.setGroupId(ticketReportTableDTO.getTicketId());
							issuededTicketGroup.setGroupName(ticketReportTableDTO.getVisitorName());
							issuededTicketGroup.setCount(ticketReportTableDTO.getPersons());
							issuededTicketGroup.setPrice(ticketReportTableDTO.getPrice());
							issuededTicketGroup.setPersonCount(ticketReportTableDTO.getPersons());
							issuededTicketGroup.setTicketSerial(ticketReportTableDTO.getTicketSerial());
		
							tickets[ISSUED_TICKET].getGroup().put(Long.valueOf(visitorIdToIndex.get(ticketReportTableDTO.getVisitorId())), issuededTicketGroup);
						}
						else {
							issuededTicketGroup.setCount(issuededTicketGroup.getCount() + ticketReportTableDTO.getPersons());
							tickets[ISSUED_TICKET].setSubTotal(tickets[ISSUED_TICKET].getSubTotal() + (ticketReportTableDTO.getPersons() * ticketReportTableDTO.getPrice()));
						}
					}
				}
			}
		}

		// Make object with zero values that were not present
		for (HashMap.Entry<Long, RSCBReportTicket[]> element: row.entrySet()) {
			for (short index = 0; index < element.getValue().length; ++index) {
				for (HashMap.Entry<Long, Integer> mappedIndex: visitorIdToIndex.entrySet()) {
					if (element.getValue()[index] == null) {
						RSCBReportTicket ticket = new RSCBReportTicket();
						ticket.setTicketId(0L);
						ticket.setTicketName("");
						ticket.setSubTotal(0);
						ticket.setGroup(new HashMap<Long, RSCBReportGroup>());
						element.getValue()[index] = ticket;
					}
					if (element.getValue()[index].getGroup().get(Long.valueOf(mappedIndex.getValue())) == null) {
						RSCBReportGroup group = new RSCBReportGroup();
						group.setGroupName("");
						group.setCount(0);
						group.setPrice(0f);
						group.setPersonCount(0);
						element.getValue()[index].getGroup().put(Long.valueOf(mappedIndex.getValue()), group);
					}
				}
			}
		}
		return row;
	}
}
