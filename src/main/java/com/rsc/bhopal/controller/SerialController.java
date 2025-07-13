package com.rsc.bhopal.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rsc.bhopal.dtos.ApplicationConstantDTO;
import com.rsc.bhopal.dtos.ResponseMessage;
import com.rsc.bhopal.service.ApplicationConstantService;
import com.rsc.bhopal.utills.CommonUtills;
import com.rsc.bhopal.utills.SerialRangeValidation;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/manage/serial")
@Slf4j
public class SerialController {
	@Autowired
	private ApplicationConstantService applicationConstantService;

	@GetMapping(path = "")
	public String serialPageGet(Model model) {
		ApplicationConstantDTO applicationConstantDTO;
		applicationConstantDTO = applicationConstantService.getTicketSerial();
		model.addAttribute("ticket_serial_id", applicationConstantDTO.getId());
		model.addAttribute("ticket_serial", applicationConstantDTO.getData());
		applicationConstantDTO =  applicationConstantService.getBillSeries();
		model.addAttribute("bill_series_id", applicationConstantDTO.getId());
		model.addAttribute("bill_series", applicationConstantDTO.getData());
		applicationConstantDTO = applicationConstantService.getBillSerialStart();
		model.addAttribute("bill_serial_start_id", applicationConstantDTO.getId());
		model.addAttribute("bill_serial_start", applicationConstantDTO.getData());
		applicationConstantDTO = applicationConstantService.getBillSerialEnd();
		model.addAttribute("bill_serial_end_id", applicationConstantDTO.getId());
		model.addAttribute("bill_serial_end", applicationConstantDTO.getData());
		applicationConstantDTO = applicationConstantService.getBillSerial();
		model.addAttribute("bill_serial_id", applicationConstantDTO.getId());
		model.addAttribute("bill_serial", applicationConstantDTO.getData());
		return "admin/serial";
	}

	@PostMapping(path = "/ticket-serial")
	public String ticketSerialPost(@RequestParam("ticket-serial-id") String ticketSerialId, @RequestParam("ticket-serial") String ticketSerial, RedirectAttributes redirectAttributes) throws JsonProcessingException {
		if (ticketSerialId != null && ticketSerial != null) {
			applicationConstantService.replaceTicketSerial(Long.valueOf(ticketSerialId), ticketSerial);
			redirectAttributes.addFlashAttribute("message", CommonUtills.convertToJSON(ResponseMessage.builder().status(true).message("Serial changed.").build()));
		}
		else {
			redirectAttributes.addFlashAttribute("message", CommonUtills.convertToJSON(ResponseMessage.builder().status(false).message("Error.").build()));
		}
		return "redirect:../serial";
	}

	@PostMapping(path = "/bill-series")
	public String billSeriesPost(@RequestParam("bill-series-id") String billSeriesId, @RequestParam("bill-series") String billSeries, RedirectAttributes redirectAttributes) throws JsonProcessingException {
		if (billSeriesId != null && billSeries != null) {
			applicationConstantService.replaceBillSeries(Long.valueOf(billSeriesId), billSeries);
			redirectAttributes.addFlashAttribute("message", CommonUtills.convertToJSON(ResponseMessage.builder().status(true).message("Serial changed.").build()));
		}
		else {
			redirectAttributes.addFlashAttribute("message", CommonUtills.convertToJSON(ResponseMessage.builder().status(false).message("Error.").build()));
		}
		return "redirect:../serial";
	}

	@PostMapping(path = "/bill-serial")
	public String billSerialPost(@RequestParam("bill-serial-id") Long billSerialId, @RequestParam("bill-serial") Long billSerial, @RequestParam("bill-serial-start-id") Long billSerialStartId, @RequestParam("bill-serial-start") Long billSerialStart, @RequestParam("bill-serial-end-id") Long billSerialEndId, @RequestParam("bill-serial-end") Long billSerialEnd, RedirectAttributes redirectAttributes) throws JsonProcessingException {
		final long prevCurrentSerial = Long.parseLong(applicationConstantService.getBillSerial().getData());
		final long prevStartSerial = Long.parseLong(applicationConstantService.getBillSerialStart().getData());
		final long prevEndSerial = Long.parseLong(applicationConstantService.getBillSerialEnd().getData());

		// final boolean serialRangeValidation = new SerialRangeValidation(prevCurrentSerial, prevStartSerial, prevEndSerial, billSerial, billSerialStart, billSerialEnd).validate();
		if ((billSerialStart <= billSerial) && (billSerial <= billSerialEnd) && (billSerialStart <= billSerialEnd)) {
			applicationConstantService.replaceBillSerial(billSerialId, String.valueOf(billSerial));
			applicationConstantService.replaceBillSerialStart(billSerialStartId, String.valueOf(billSerialStart));
			applicationConstantService.replaceBillSerialEnd(billSerialEndId, String.valueOf(billSerialEnd));
			redirectAttributes.addFlashAttribute("message", CommonUtills.convertToJSON(ResponseMessage.builder().status(true).message("Serials changed.").build()));
		}
		else {
			redirectAttributes.addFlashAttribute("message", CommonUtills.convertToJSON(ResponseMessage.builder().status(false).message("Check serials inputs.").build()));
		}

		/*
		StringBuilder toastMessage = new StringBuilder("");
		if ((billSerialId != null) && (billSerial != null) && (!billSerial.isEmpty())) {
			applicationConstantService.replaceBillSerial(Long.valueOf(billSerialId), billSerial);
			toastMessage.append("Serial changed to " + billSerial + ".\n");
		}
		if ((billSerialStartId != null) && (billSerialStart != null) && (!billSerialStart.isEmpty())) {
			applicationConstantService.replaceBillSerialStart(Long.valueOf(billSerialStartId), billSerialStart);
			toastMessage.append("Changed to " + billSerialStart + ".\n");
		}
		if ((billSerialEndId != null) && (billSerialEnd != null) && (!billSerialEnd.isEmpty())) {
			applicationConstantService.replaceBillSerialEnd(Long.valueOf(billSerialEndId), billSerialEnd);
			toastMessage.append("Changed to " + billSerialEnd + ".\n");
		}
		if (toastMessage.isEmpty()) {
			redirectAttributes.addFlashAttribute("message", CommonUtills.convertToJSON(ResponseMessage.builder().status(false).message("None changed.").build()));
		}
		else {
			redirectAttributes.addFlashAttribute("message", CommonUtills.convertToJSON(ResponseMessage.builder().status(true).message(toastMessage.toString()).build()));
		}
		*/

		return "redirect:../serial";
	}
}
