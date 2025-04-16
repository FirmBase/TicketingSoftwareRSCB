package com.rsc.bhopal.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/manage/serial")
@Slf4j
public class SerialController {
	@Autowired
	private ApplicationConstantService applicationConstantService;

	private Long ticketSerialId;

	@GetMapping(path = "")
	public String serialPageGet(Map<String, Object> attributes) {
		String serial = null;
		for (ApplicationConstantDTO applicationConstantDTO: applicationConstantService.getAllTicketsSerial()) {
			ticketSerialId = applicationConstantDTO.getId();
			serial = applicationConstantDTO.getData();
		}
		attributes.put("serial", serial);
		return "admin/serial";
	}

	@PostMapping(path = "")
	public String serialPagePost(@RequestParam("serial") String serial, RedirectAttributes redirectAttributes) throws JsonProcessingException {
		if (ticketSerialId != null) {
			applicationConstantService.replaceTicketSerial(Long.valueOf(ticketSerialId), serial);
		}
		redirectAttributes.addFlashAttribute("message", CommonUtills.convertToJSON(ResponseMessage.builder().status(true).message("Serial changed.").build()));
		return "redirect:serial";
	}
}
