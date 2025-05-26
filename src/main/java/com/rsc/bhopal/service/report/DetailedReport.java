package com.rsc.bhopal.service.report;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.rsc.bhopal.dtos.report.BillDate;
import com.rsc.bhopal.dtos.report.Group;
import com.rsc.bhopal.dtos.report.Parking;
import com.rsc.bhopal.dtos.report.Ticket;
import com.rsc.bhopal.enums.BillType;
import com.rsc.bhopal.enums.GroupType;
import com.rsc.bhopal.projections.TicketDailyReport;

public class DetailedReport {
	public LinkedHashMap<Date, BillDate> arrange(List<TicketDailyReport> ticketDailyReports, Map<Long, String> ticketsMap, Map<Long, String> visitorsSingleMap, Map<Long, String> visitorsComboMap, Map<Long, String> parkingsMap, double[] grandTotal) {
		int ticketCount = 0;
		LinkedHashMap<Long, Integer> ticketIdToIndex = new LinkedHashMap<Long, Integer>();
		for (Map.Entry<Long, String> ticketMap: ticketsMap.entrySet()) {
			ticketIdToIndex.put(ticketMap.getKey(), ticketCount++);
		}
		int visitorSingleCount = 0;
		LinkedHashMap<Long, Integer> groupSingleIdToIndex = new LinkedHashMap<Long, Integer>();
		for (Map.Entry<Long, String> visitorSingleMap: visitorsSingleMap.entrySet()) {
			groupSingleIdToIndex.put(visitorSingleMap.getKey(), visitorSingleCount++);
		}
		int visitorComboCount = 0;
		LinkedHashMap<Long, Integer> groupComboIdToIndex = new LinkedHashMap<Long, Integer>();
		for (Map.Entry<Long, String> visitorComboMap: visitorsComboMap.entrySet()) {
			groupComboIdToIndex.put(visitorComboMap.getKey(), visitorComboCount++);
		}
		int parkingCount = 0;
		LinkedHashMap<Long, Integer> parkingIdToIndex = new LinkedHashMap<Long, Integer>();
		for (Map.Entry<Long, String> parkingMap: parkingsMap.entrySet()) {
			parkingIdToIndex.put(parkingMap.getKey(), parkingCount++);
		}

		LinkedHashMap<Date, BillDate> billDates = new LinkedHashMap<Date, BillDate>();
		for (TicketDailyReport ticketDailyReport: ticketDailyReports) {
			BillDate billDate = billDates.get(ticketDailyReport.getBillDate());
			if (billDate == null) {
				billDate = new BillDate();
				billDate.setDate(ticketDailyReport.getBillDate());
				billDate.setParkings(new HashMap<Long, Parking>());
				billDate.setGroups(new HashMap<Long, Group>());
				billDate.setTickets(new HashMap<Long, Ticket>());
				billDate.setTotalTickets(0);
				billDate.setTotalAmount(0d);

				if (ticketDailyReport.getBillType() == BillType.PARKING) {
					// billDate.setTotalTickets(0);	// Assuming parking is not a ticket
					// billDate.setTotalTickets((int) Math.ceil(ticketDailyReport.getTotalSum() / ticketDailyReport.getPrice()));	// Assuming parking is a ticket
					// billDate.setTotalAmount(ticketDailyReport.getTotalSum());

					Parking parking = new Parking();
					parking.setTotalRate(ticketDailyReport.getTotalSum());
					parking.setTotalCount((int) Math.ceil(ticketDailyReport.getTotalSum() / ticketDailyReport.getPrice()));
					billDate.getParkings().put(Long.valueOf(parkingIdToIndex.get(ticketDailyReport.getParkingDetId())), parking);
				}
				else if (ticketDailyReport.getGroupType() == GroupType.COMBO) {
					// billDate.setTotalTickets(1);
					// billDate.setTotalAmount(Double.valueOf(ticketDailyReport.getPrice()));
					// billDate.setTotalTickets(1);	// If Combo is just 1 ticket

					Group group = new Group();
					group.setGroupId(ticketDailyReport.getVisitorId());
					group.setGroupName(ticketDailyReport.getVisitorName());
					group.setTicketSerial(ticketDailyReport.getTicketSerial());
					group.setQuantity(1);
					group.setAmount(ticketDailyReport.getPrice());
					billDate.getGroups().put(Long.valueOf(groupComboIdToIndex.get(ticketDailyReport.getVisitorId())), group);
				}
				else {
					// billDate.setTotalTickets(ticketDailyReport.getPersons());
					// billDate.setTotalAmount(Double.valueOf(ticketDailyReport.getPrice() * ticketDailyReport.getPersons()));

					Ticket ticket = new Ticket();
					ticket.setTicketId(ticketDailyReport.getTicketId());
					ticket.setTicketName(ticketDailyReport.getTicketName());
					ticket.setTotalQuantity(ticketDailyReport.getPersons());
					ticket.setTotalAmount(Double.valueOf(ticketDailyReport.getPrice() * ticketDailyReport.getPersons()));
					ticket.setGroups(new HashMap<Long, Group>());

					Group group = new Group();
					group.setGroupId(ticketDailyReport.getVisitorId());
					group.setGroupName(ticketDailyReport.getVisitorName());
					group.setTicketSerial(ticketDailyReport.getTicketSerial());
					group.setQuantity(ticketDailyReport.getPersons());
					group.setAmount(ticketDailyReport.getPrice() * ticketDailyReport.getPersons());
					ticket.getGroups().put(Long.valueOf(groupSingleIdToIndex.get(ticketDailyReport.getVisitorId())), group);

					billDate.getTickets().put(Long.valueOf(ticketIdToIndex.get(ticketDailyReport.getTicketId())), ticket);
				}

				billDates.put(ticketDailyReport.getBillDate(), billDate);
			}
			else {
				if (ticketDailyReport.getBillType() == BillType.PARKING) {
					Parking parking = billDate.getParkings().get(Long.valueOf(parkingIdToIndex.get(ticketDailyReport.getParkingDetId())));
					if (parking == null) {
						parking = new Parking();
						parking.setParkingId(ticketDailyReport.getParkingDetId());
						parking.setParkingName(ticketDailyReport.getParkingDetName());
						parking.setTotalCount((int) Math.ceil(ticketDailyReport.getTotalSum() / ticketDailyReport.getPrice()));
						parking.setTotalRate(ticketDailyReport.getTotalSum());
						billDate.getParkings().put(Long.valueOf(parkingIdToIndex.get(ticketDailyReport.getParkingDetId())), parking);
					}
					else {
						parking.setTotalCount(parking.getTotalCount() + (int) Math.ceil(ticketDailyReport.getTotalSum() / ticketDailyReport.getPrice()));
						parking.setTotalRate(parking.getTotalRate() + ticketDailyReport.getTotalSum());
					}
				}
				else if (ticketDailyReport.getGroupType() == GroupType.COMBO) {
					Group group = billDate.getGroups().get(Long.valueOf(groupComboIdToIndex.get(ticketDailyReport.getVisitorId())));
					if (group == null) {
						group = new Group();
						group.setGroupId(ticketDailyReport.getVisitorId());
						group.setGroupName(ticketDailyReport.getVisitorName());
						group.setTicketSerial(ticketDailyReport.getTicketSerial());
						group.setQuantity(1);
						group.setAmount(ticketDailyReport.getPrice());
						billDate.getGroups().put(Long.valueOf(groupComboIdToIndex.get(ticketDailyReport.getVisitorId())), group);
					}
					else {
						group.setQuantity(group.getQuantity() + 1);	// Assuming Each Combo is an individual ticket
						group.setAmount(group.getAmount() + ticketDailyReport.getPrice());
					}
				}
				else {
					Ticket ticket = billDate.getTickets().get(Long.valueOf(ticketIdToIndex.get(ticketDailyReport.getTicketId())));
					if (ticket == null) {
						ticket = new Ticket();
						ticket.setTicketId(ticketDailyReport.getTicketId());
						ticket.setTicketName(ticketDailyReport.getTicketName());
						ticket.setTotalQuantity(ticketDailyReport.getPersons());
						ticket.setTotalAmount(Double.valueOf(ticketDailyReport.getPrice() * ticketDailyReport.getPersons()));
						ticket.setGroups(new HashMap<Long, Group>());

						Group group = new Group();
						group.setGroupId(ticketDailyReport.getVisitorId());
						group.setGroupName(ticketDailyReport.getVisitorName());
						group.setTicketSerial(ticketDailyReport.getTicketSerial());
						group.setQuantity(ticketDailyReport.getPersons());
						group.setAmount(ticketDailyReport.getPrice() * ticketDailyReport.getPersons());
						ticket.getGroups().put(Long.valueOf(groupSingleIdToIndex.get(ticketDailyReport.getVisitorId())), group);

						billDate.getTickets().put(Long.valueOf(ticketIdToIndex.get(ticketDailyReport.getTicketId())), ticket);
					}
					else {
						Group group = ticket.getGroups().get(Long.valueOf(groupSingleIdToIndex.get(ticketDailyReport.getVisitorId())));
						if (group == null) {
							group = new Group();
							group.setGroupId(ticketDailyReport.getVisitorId());
							group.setGroupName(ticketDailyReport.getVisitorName());
							group.setTicketSerial(ticketDailyReport.getTicketSerial());
							group.setQuantity(ticketDailyReport.getPersons());
							group.setAmount(ticketDailyReport.getPrice() * ticketDailyReport.getPersons());
							ticket.getGroups().put(Long.valueOf(groupSingleIdToIndex.get(ticketDailyReport.getVisitorId())), group);
						}
						else {
							group.setQuantity(group.getQuantity() + ticketDailyReport.getPersons());
							group.setAmount(group.getAmount() + (ticketDailyReport.getPrice() * ticketDailyReport.getPersons()));
						}
						ticket.setTotalQuantity(ticket.getTotalQuantity() + ticketDailyReport.getPersons());
						ticket.setTotalAmount(ticket.getTotalAmount() + (ticketDailyReport.getPrice() * ticketDailyReport.getPersons()));
					}
				}
			}
			if (ticketDailyReport.getBillType() == BillType.PARKING) {
				// billDate.setTotalTickets(billDate.getTotalTickets() + (int) Math.ceil(ticketDailyReport.getTotalSum() / ticketDailyReport.getPrice()));	// Assuming Parking is not a ticket
				billDate.setTotalAmount(billDate.getTotalAmount() + ticketDailyReport.getTotalSum());

				grandTotal[0] += ticketDailyReport.getTotalSum();
			}
			else if (ticketDailyReport.getGroupType() == GroupType.COMBO) {
				billDate.setTotalTickets(billDate.getTotalTickets() + 1);	// Assuming Each Combo is an individual ticket
				billDate.setTotalAmount(billDate.getTotalAmount() + ticketDailyReport.getPrice());

				grandTotal[0] += ticketDailyReport.getPrice();
			}
			else {
				billDate.setTotalTickets(billDate.getTotalTickets() + ticketDailyReport.getPersons());
				billDate.setTotalAmount(billDate.getTotalAmount() + (ticketDailyReport.getPrice() * ticketDailyReport.getPersons()));

				grandTotal[0] += ticketDailyReport.getPrice() * ticketDailyReport.getPersons();
			}
		}

		// Make object with zero values that were not present
		for (HashMap.Entry<Date, BillDate> billDate: billDates.entrySet()) {
			for (HashMap.Entry<Long, Integer> groupComboMappedIndex: groupComboIdToIndex.entrySet()) {
				if (billDate.getValue().getGroups().get(Long.valueOf(groupComboMappedIndex.getValue())) == null) {
					Group group = new Group();
					group.setGroupName("");
					group.setQuantity(0);
					group.setAmount(0f);
					billDate.getValue().getGroups().put(Long.valueOf(groupComboMappedIndex.getValue()), group);
				}
			}
			for (HashMap.Entry<Long, Integer> parkingMappedIndex: parkingIdToIndex.entrySet()) {
				if (billDate.getValue().getParkings().get(Long.valueOf(parkingMappedIndex.getValue())) == null) {
					Parking parking = new Parking();
					parking.setParkingName("");
					parking.setTotalCount(0);
					parking.setTotalRate(0d);
					billDate.getValue().getParkings().put(Long.valueOf(parkingMappedIndex.getValue()), parking);
				}
			}
			for (HashMap.Entry<Long, Integer> ticketMappedIndex: ticketIdToIndex.entrySet()) {
				if (billDate.getValue().getTickets().get(Long.valueOf(ticketMappedIndex.getValue())) == null) {
					Ticket ticket = new Ticket();
					ticket.setTicketName("");
					ticket.setTotalQuantity(0);
					ticket.setTotalAmount(0d);
					ticket.setGroups(new LinkedHashMap<Long, Group>());
					for (HashMap.Entry<Long, Integer> groupSingleMappedIndex: groupSingleIdToIndex.entrySet()) {
						Group group = new Group();
						group.setGroupName("");
						group.setQuantity(0);
						group.setAmount(0f);
						ticket.getGroups().put(Long.valueOf(groupSingleMappedIndex.getValue()), group);
					}
					billDate.getValue().getTickets().put(Long.valueOf(ticketMappedIndex.getValue()), ticket);
				}
				else {
					for (HashMap.Entry<Long, Integer> groupSingleMappedIndex: groupSingleIdToIndex.entrySet()) {
						if (billDate.getValue().getTickets().get(Long.valueOf(ticketMappedIndex.getValue())).getGroups().get(Long.valueOf(groupSingleMappedIndex.getValue())) == null) {
							Group group = new Group();
							group.setGroupName("");
							group.setQuantity(0);
							group.setAmount(0f);
							billDate.getValue().getTickets().get(Long.valueOf(ticketMappedIndex.getValue())).getGroups().put(Long.valueOf(groupSingleMappedIndex.getValue()), group);
						}
					}
				}
			}
		}
		return billDates;
	}
}
