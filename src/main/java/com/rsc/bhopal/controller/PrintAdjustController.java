package com.rsc.bhopal.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rsc.bhopal.dtos.PrintAdjustDTO;
import com.rsc.bhopal.dtos.PrintAdjustWrapperListDTO;
import com.rsc.bhopal.service.ApplicationConstantService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequestMapping("/manage")
@Slf4j
public class PrintAdjustController {
	@Autowired
	private ApplicationConstantService applicationConstantService;

	@GetMapping(path = "/print/adjust")
	public String getPage(Map<String, Object> attributes) {
		attributes.put("printAdjustList", applicationConstantService.getAllCurrentPrintCoordinate());
		// return "admin/print_adjustment";
		return "admin/print";
	}

	@PostMapping("/print/adjust")
	public String processFields(@ModelAttribute PrintAdjustWrapperListDTO printAdjustDTOs) {
		// log.debug("Print Adjust: " + printAdjustDTOs);
		printAdjustDTOs.getPrintAdjustDTOs().forEach(printAdjustDTO -> {
			// printAdjustDTO.setTop(printAdjustDTO.getTop() == null ? 0 : printAdjustDTO.getTop());
			// printAdjustDTO.setLeft(printAdjustDTO.getLeft() == null ? 0 : printAdjustDTO.getLeft());
			// printAdjustDTO.setHeight(printAdjustDTO.getHeight() == null ? 0 : printAdjustDTO.getHeight());
			// printAdjustDTO.setWidth(printAdjustDTO.getTop() == null ? 0 : printAdjustDTO.getWidth());
		});
		applicationConstantService.setAllCurrentPrintCoordinates(printAdjustDTOs.getPrintAdjustDTOs());
		return "redirect:/manage/print/adjust";
	}
}
