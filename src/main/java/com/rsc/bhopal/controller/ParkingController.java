package com.rsc.bhopal.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rsc.bhopal.dtos.ParkingDetailsDTO;
import com.rsc.bhopal.dtos.ParkingPriceDTO;
import com.rsc.bhopal.dtos.ParkingPriceDTOWrapper;
import com.rsc.bhopal.dtos.TicketsRatesMasterDTO;
import com.rsc.bhopal.service.ParkingService;
import com.rsc.bhopal.service.TicketsRatesService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("/manage")
@Slf4j
public class ParkingController {
	@Autowired
	private ParkingService parkingService;

	@Autowired
	private TicketsRatesService ticketsRatesService;

	@GetMapping(path = "/parking/add")
	public String addParkingDetails(@ModelAttribute ParkingPriceDTO parkingPriceDTO, Map<String, Object> attributes) {
		attributes.put("parkings", parkingService.getParkingDetails());
		return "parking/add";
	}

	@PostMapping(path = "/parking/add")
	public String addNewParkingDetails(@ModelAttribute ParkingPriceDTO parkingPriceDTO, Principal user, Map<String, Object> attributes) {
		// parkingService.addNewParking(parkingPriceDTO);
		parkingService.addNewParkingRate(parkingPriceDTO, user);
		// log.debug(parkingPriceDTO.toString());
		attributes.put("parkings", parkingService.getParkingDetails());
		return "redirect:/manage/parking/add";
	}

	@PostMapping(path = "/parking/price/change")
	public String updateParkingRate(@ModelAttribute ParkingPriceDTO parkingPriceDTO) {
		ticketsRatesService.updateParkingRate(parkingPriceDTO);
		return "redirect:/manage/parking/add";
	}

	@PostMapping("/parking/status")
	public String changeStatus(@RequestParam("parkingId") Long parkingId) {
		parkingService.changeParkingStatus(parkingId);
		return "redirect:/manage/parking/add";
	}

	@GetMapping(path = "/parking/details")
	@ResponseBody
	public List<ParkingDetailsDTO> getDetails() {
		return parkingService.getParkingDetails();
	}
}
