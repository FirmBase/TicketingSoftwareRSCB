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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rsc.bhopal.dtos.VisitorsTypeDTO;
import com.rsc.bhopal.enums.GroupType;
import com.rsc.bhopal.service.VisitorTypeService;
import com.rsc.bhopal.utills.CommonUtills;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/manage/groups")
public class GroupController {

	@Autowired
	VisitorTypeService visitorTypeService;

	@GetMapping(path = "/add")
	public String addGroup(Map<String,Object> attributes) {
		List<VisitorsTypeDTO> visitors = visitorTypeService.getAllVisitorTypes();
		attributes.put("visitors", visitors);
		attributes.put("GroupType", GroupType.class);
		return "groups/add";
	}

	@PostMapping(path = "/add")
	public String addGroup(@ModelAttribute VisitorsTypeDTO dto,Principal user ) throws JsonProcessingException {
		log.debug("DTO "+CommonUtills.convertToJSON(dto));
		// Disbaled field form might not send 0 value for Minimum Members
		if (dto.getGroupType() == GroupType.COMBO)
			dto.setMinMembers(dto.getMinMembers() == null ? 0 : dto.getMinMembers());
		// Disabled field form might not send 0 value for Fixed Members
		if (dto.getGroupType() == GroupType.SINGLE)
			dto.setFixedMembers(dto.getFixedMembers() == null ? 0 : dto.getFixedMembers());
		visitorTypeService.addVisitorType(dto, user.getName());
		return "redirect:/manage/groups/add";
	}

	@PostMapping("/status")
	public String changeStatus(@RequestParam("vistiorId") Long visitorId) {
		visitorTypeService.changeVisitorStatus(visitorId);
		return "redirect:/manage/groups/add";
	}

	@PostMapping("/make-default")
	public String changeDefault(@RequestParam("vistiorId") Long visitorId) {
		visitorTypeService.changeDefaultVisitor(visitorId);
		return "redirect:/manage/groups/add";
	}
}
