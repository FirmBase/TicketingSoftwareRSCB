package com.rsc.bhopal.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rsc.bhopal.dtos.ParkingDetailsDTO;
import com.rsc.bhopal.dtos.TicketDetailsDTO;
import com.rsc.bhopal.dtos.VisitorsTypeDTO;
import com.rsc.bhopal.enums.BillType;
import com.rsc.bhopal.enums.GroupType;
import com.rsc.bhopal.projections.TicketDailyReport;
import com.rsc.bhopal.repos.TicketBillRowRepository;
import com.rsc.bhopal.service.ParkingService;
import com.rsc.bhopal.service.TicketDetailsService;
import com.rsc.bhopal.service.VisitorTypeService;
import com.rsc.bhopal.utills.DailyReportExcel;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequestMapping("/report")
public class TicketDailyReportController {
	@Autowired
	private TicketBillRowRepository ticketBillRowRepository;

	@Autowired
	private TicketDetailsService ticketDetailsService;

	@Autowired
	private VisitorTypeService visitorTypeService;

	@Autowired
	private ParkingService parkingService;

	/*
	@GetMapping(path = "/year-wise/{year}")
	public LinkedHashMap<Date, BillDate> TicketDailyReportController(@PathVariable("year") Short year) {
		List<TicketDailyReport> ticketDailyReports = ticketBillRowRepository.getDailyReportDetails(year);
		ticketDetailsService.getAllTickets().stream().map(TicketDetailsDTO::getName).collect(Collectors.toList()).forEach(ticketDetailDTO -> {
			log.debug(ticketDetailDTO.toString());
		});
		visitorTypeService.getAllVisitorTypes().stream().filter(visitorsTypeDTO -> GroupType.SINGLE.equals(visitorsTypeDTO.getGroupType())).map(VisitorsTypeDTO::getName).collect(Collectors.toList()).forEach(visitorTypeDTO -> {
			log.debug(visitorTypeDTO.toString());
		});
		final Map<Long, String> ticketsMap = ticketDetailsService.getAllTickets().stream().collect(Collectors.toMap(TicketDetailsDTO::getId, TicketDetailsDTO::getName));
		final Map<Long, String> visitorsSingleMap = visitorTypeService.getAllVisitorTypes().stream().filter(visitorsTypeDTO -> GroupType.SINGLE.equals(visitorsTypeDTO.getGroupType())).collect(Collectors.toMap(VisitorsTypeDTO::getId, VisitorsTypeDTO::getName));
		final Map<Long, String> visitorsComboMap = visitorTypeService.getAllVisitorTypes().stream().filter(visitorsTypeDTO -> GroupType.COMBO.equals(visitorsTypeDTO.getGroupType())).collect(Collectors.toMap(VisitorsTypeDTO::getId, VisitorsTypeDTO::getName));
		final Map<Long, String> parkingsMap = parkingService.getParkingDetails().stream().collect(Collectors.toMap(ParkingDetailsDTO::getId, ParkingDetailsDTO::getName));

		double []grandTotal = new double[1];
		return arrange(ticketDailyReports, ticketsMap, visitorsSingleMap, visitorsComboMap, parkingsMap, grandTotal);
	}
	*/

	@GetMapping(path = "/year-wise/{year}")
	public String TicketDailyReportController(@PathVariable("year") Short year, Map<String, Object> attributes) {
		List<TicketDailyReport> ticketDailyReports = ticketBillRowRepository.getDailyReportDetails(year);
		attributes.put("startDateTime", "");
		attributes.put("endDateTime", "");
		attributes.put("targetDate", year);
		final Map<Long, String> ticketsMap = ticketDetailsService.getAllTickets().stream().collect(Collectors.toMap(TicketDetailsDTO::getId, TicketDetailsDTO::getName));
		final List<VisitorsTypeDTO> vistitorsList = visitorTypeService.getAllActiveVisitorTypes();
		final Map<Long, String> visitorsSingleMap = vistitorsList.stream().filter(visitorsTypeDTO -> GroupType.SINGLE.equals(visitorsTypeDTO.getGroupType())).collect(Collectors.toMap(VisitorsTypeDTO::getId, VisitorsTypeDTO::getName));
		final Map<Long, String> visitorsComboMap = vistitorsList.stream().filter(visitorsTypeDTO -> GroupType.COMBO.equals(visitorsTypeDTO.getGroupType())).collect(Collectors.toMap(VisitorsTypeDTO::getId, VisitorsTypeDTO::getName));
		final Map<Long, String> parkingsMap = parkingService.getParkingDetails().stream().collect(Collectors.toMap(ParkingDetailsDTO::getId, ParkingDetailsDTO::getName));
		// final Map<Long, String> visitorsMap = visitorTypeService.getAllVisitorTypes().stream().collect(Collectors.toMap(VisitorsTypeDTO::getId, VisitorsTypeDTO::getName));
		attributes.put("ticketsName", ticketsMap.values().stream().collect(Collectors.toList()));
		attributes.put("visitorsSingleName", visitorsSingleMap.values().stream().collect(Collectors.toList()));
		attributes.put("visitorsComboName", visitorsComboMap.values().stream().collect(Collectors.toList()));
		attributes.put("parkingsName", parkingsMap.values().stream().collect(Collectors.toList()));
		double []grandTotal = new double[1];
		attributes.put("bills", arrange(ticketDailyReports, ticketsMap, visitorsSingleMap, visitorsComboMap, parkingsMap, grandTotal));
		attributes.put("ticketSerials", new int[2]);
		attributes.put("grandTotal", grandTotal[0]);
		return "reports/date";
	}

	@PostMapping(value = "/year-wise")
	public String redirect(@RequestParam Short year) {
		return "redirect:/report/year-wise/" + year;
	}

	@PostMapping(value = "/year-wise/{year}", produces = MediaType.APPLICATION_PDF_VALUE)
	public void generateSheet(@PathVariable("year") Short year, HttpServletResponse httpServletResponse) throws IOException {
		// httpServletResponse.setContentType("application/xlsx");
		httpServletResponse.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
		// httpServletResponse.setHeader("Content-Disposition", "inline; filename=daily_report.xlxs");
		httpServletResponse.setHeader("Content-Disposition", "attachment; filename=daily_report.xlsx");
		List<TicketDailyReport> ticketDailyReports = ticketBillRowRepository.getDailyReportDetails(year);
		final Map<Long, String> ticketsMap = ticketDetailsService.getAllTickets().stream().collect(Collectors.toMap(TicketDetailsDTO::getId, TicketDetailsDTO::getName));
		final List<VisitorsTypeDTO> visitorsList = visitorTypeService.getAllVisitorTypes();
		final Map<Long, String> visitorsSingleMap = visitorsList.stream().filter(visitorsTypeDTO -> GroupType.SINGLE.equals(visitorsTypeDTO.getGroupType())).collect(Collectors.toMap(VisitorsTypeDTO::getId, VisitorsTypeDTO::getName));
		final Map<Long, String> visitorsComboMap = visitorsList.stream().filter(visitorsTypeDTO -> GroupType.COMBO.equals(visitorsTypeDTO.getGroupType())).collect(Collectors.toMap(VisitorsTypeDTO::getId, VisitorsTypeDTO::getName));
		final Map<Long, String> parkingsMap = parkingService.getParkingDetails().stream().collect(Collectors.toMap(ParkingDetailsDTO::getId, ParkingDetailsDTO::getName));
		final DailyReportExcel dailyReportExcel = new DailyReportExcel(ticketDailyReports, ticketsMap, visitorsSingleMap, visitorsComboMap, parkingsMap, httpServletResponse);
	}

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

	/*
	public LinkedHashMap<Date, BillDate> arrange(List<TicketDailyReport> ticketDailyReports, Map<Long, String> ticketsMap, Map<Long, String> visitorsMap, double[] grandTotal) {
		int ticketCount = 0;
		LinkedHashMap<Long, Integer> ticketIdToIndex = new LinkedHashMap<Long, Integer>();
		for (Map.Entry<Long, String> ticketMap: ticketsMap.entrySet()) {
			ticketIdToIndex.put(ticketMap.getKey(), ++ticketCount);
		}
		int visitorCount = 0;
		LinkedHashMap<Long, Integer> groupIdToIndex = new LinkedHashMap<Long, Integer>();
		for (Map.Entry<Long, String> visitorMap: visitorsMap.entrySet()) {
			groupIdToIndex.put(visitorMap.getKey(), ++visitorCount);
		}
		// log.debug("" + ticketIdToIndex + ", " + groupIdToIndex);

		LinkedHashMap<Date, BillDate> billDates = new LinkedHashMap<Date, BillDate>();
		for (TicketDailyReport ticketDailyReport: ticketDailyReports) {
			BillDate billDate = billDates.get(ticketDailyReport.getBillDate());
			if (billDate == null) {
				billDate = new BillDate();
				billDate.setDate(ticketDailyReport.getBillDate());
				billDate.setTotalTickets(ticketDailyReport.getPersons());
				billDate.setTotalAmount(Double.valueOf(ticketDailyReport.getPrice() * ticketDailyReport.getPersons()));
				billDate.setTickets(new HashMap<Long, Ticket>());

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
				ticket.getGroups().put(Long.valueOf(ticketDailyReport.getVisitorId()), group);

				billDate.getTickets().put(Long.valueOf(ticketIdToIndex.get(ticketDailyReport.getTicketId())), ticket);

				billDates.put(ticketDailyReport.getBillDate(), billDate);
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
					ticket.getGroups().put(Long.valueOf(ticketDailyReport.getVisitorId()), group);

					billDate.getTickets().put(Long.valueOf(ticketIdToIndex.get(ticketDailyReport.getTicketId())), ticket);
				}
				else {
					Group group = ticket.getGroups().get(Long.valueOf(groupIdToIndex.get(ticketDailyReport.getVisitorId())));
					if (group == null) {
						group = new Group();
						group.setGroupId(ticketDailyReport.getVisitorId());
						group.setGroupName(ticketDailyReport.getVisitorName());
						group.setTicketSerial(ticketDailyReport.getTicketSerial());
					}
					else {
						group.setQuantity(group.getQuantity() + ticketDailyReport.getPersons());
						group.setAmount(group.getAmount() + (ticketDailyReport.getPrice() * ticketDailyReport.getPersons()));
					}
					ticket.setTotalQuantity(ticket.getTotalQuantity() + ticketDailyReport.getPersons());
					ticket.setTotalAmount(ticket.getTotalAmount() + (ticketDailyReport.getPrice() * ticketDailyReport.getPersons()));
				}
				billDate.setTotalTickets(billDate.getTotalTickets() + ticketDailyReport.getPersons());
				billDate.setTotalAmount(billDate.getTotalAmount() + (ticketDailyReport.getPrice() * ticketDailyReport.getPersons()));
			}
			grandTotal[0] += ticketDailyReport.getPrice() * ticketDailyReport.getPersons();
		}

		// Make object with zero values that were not present
		for (HashMap.Entry<Date, BillDate> billDate: billDates.entrySet()) {
			for (HashMap.Entry<Long, Integer> ticketMappedIndex: ticketIdToIndex.entrySet()) {
				if (billDate.getValue().getTickets().get(Long.valueOf(ticketMappedIndex.getValue())) == null) {
					Ticket ticket = new Ticket();
					ticket.setTicketName("");
					ticket.setTotalQuantity(0);
					ticket.setTotalAmount(0d);
					ticket.setGroups(new LinkedHashMap<Long, Group>());
					for (HashMap.Entry<Long, Integer> groupMappedIndex: groupIdToIndex.entrySet()) {
						Group  group = new Group();
						group.setGroupName("");
						group.setQuantity(0);
						group.setAmount(0f);
						ticket.getGroups().put(Long.valueOf(groupMappedIndex.getValue()), group);
					}
					billDate.getValue().getTickets().put(Long.valueOf(ticketMappedIndex.getValue()), ticket);
				}
				else {
					for (HashMap.Entry<Long, Integer> groupMappedIndex: groupIdToIndex.entrySet()) {
						if (billDate.getValue().getTickets().get(Long.valueOf(ticketMappedIndex.getValue())).getGroups().get(Long.valueOf(groupMappedIndex.getValue())) == null) {
							Group group = new Group();
							group.setGroupName("");
							group.setQuantity(0);
							group.setAmount(0f);
							billDate.getValue().getTickets().get(Long.valueOf(ticketMappedIndex.getValue())).getGroups().put(Long.valueOf(groupMappedIndex.getValue()), group);
						}
					}
				}
			}
		}
		return billDates;
	}
	*/

	@NoArgsConstructor
	@AllArgsConstructor
	@Data
	private class BillDate {
		private Date date;
		private HashMap<Long, Parking> parkings;
		private HashMap<Long, Ticket> tickets;
		private Integer totalTickets;
		private Double totalAmount;
		private HashMap<Long, Group> groups;
	}
	@Data
	private class Parking {
		private Long parkingId;
		private String parkingName;
		private int totalCount;
		private Double totalRate;
	}
	@Data
	private class Ticket {
		private Long ticketId;
		private String ticketName;
		private HashMap<Long, Group> groups;
		private Integer totalQuantity;
		private Double totalAmount;
	}
	@Data
	private class Group {
		private Long groupId;
		private String groupName;
		private BigInteger ticketSerial;
		private Integer quantity;
		private Float amount;
	}
}
