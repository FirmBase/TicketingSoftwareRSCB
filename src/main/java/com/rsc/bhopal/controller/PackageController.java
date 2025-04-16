package com.rsc.bhopal.controller;

import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rsc.bhopal.dtos.PackageDetailsDTO;
import com.rsc.bhopal.service.PackageDetailsService;
import com.rsc.bhopal.service.VisitorTypeService;

import java.util.Map;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/manage")
@Slf4j
public class PackageController {
	@Autowired
	private PackageDetailsService packageDetailsService;

	@Autowired
	private VisitorTypeService visitorTypeService;

	@GetMapping(path = "/package/add")
	public String showPackages(Map<String, Object> attributes) {
		attributes.put("packages", packageDetailsService.getAllPackageDetails());
		attributes.put("groups", visitorTypeService.getAllActiveVisitorTypes());
		return "packages/add";
	}

	@PostMapping(path = "/package/add")
	public String addPackage(@ModelAttribute PackageDetailsDTO packageDetailsDTO, Principal user) {
		packageDetailsService.addPackageDetails(packageDetailsDTO, user.getName());
		return "redirect:/manage/package/add";
	}

	/*
	@PostMapping(path = "/package/add")
	public String addPackage(@ModelAttribute PackageDetailsDTO packageDetailsDTO, Principal user) {
		// packageDetailsService.addPackageDetails(packageDetailsWithVisitors.getPackageDetailsDTO(), user.getName());
		// log.debug("PackageDetailsWithVisitors: " + packageDetailsWithVisitors);
		log.debug("PackageDetailsDTO: " + packageDetailsDTO);
		return "redirect:/manage/package/add";
	}
	*/

	@PostMapping(path = "/package/toggle")
	public String toggleActiveState(@RequestParam("packageId") Long packageId) {
		if (packageId != null)
			packageDetailsService.togglePackage(packageId);
		return "redirect:/manage/package/add";
	}

	@GetMapping(path = "/package")
	public @ResponseBody List<PackageDetailsDTO> getAllPackageDetails() {
		List<PackageDetailsDTO> packageDetailsDTOs = packageDetailsService.getAllPackageDetails();
		// log.debug("All Packages: " + packageDetailsDTOs);
		return packageDetailsDTOs;
	}
}
