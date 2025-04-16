package com.rsc.bhopal.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.rsc.bhopal.aops.service.ActivityLogService;
import com.rsc.bhopal.dtos.ActivityLogDTO;
import com.rsc.bhopal.dtos.TicketRevisedSummary;
import com.rsc.bhopal.service.TicketsRatesService;

@Controller
@RequestMapping("/manage/logs")
public class LogsController {

	@Autowired
	private ActivityLogService logService;

	@Autowired
	private TicketsRatesService ratesService;

	@GetMapping(path = {"/{rows}","/"})
	public String showLogTable(@PathVariable(name = "rows", required = false) Integer rows, Map<String, Object> attributes) {
		rows = rows == null ? 10 : rows;
		List<ActivityLogDTO> LogDTOs = logService.getAllLogs(rows);
		attributes.put("logs", LogDTOs);
		return "history/activitylog/logs";
	}

	@GetMapping(path = {"/rates/{rows}"})
	public String showRevisedRates(@PathVariable(name = "rows", required = false) Integer rows, Map<String, Object> attributes) {
		rows = rows == null ? 10 : rows;
		List<TicketRevisedSummary> logSummary = ratesService.getRevisedRates();
		attributes.put("rates", logSummary);
		return "history/revisedRates";
	}
}
