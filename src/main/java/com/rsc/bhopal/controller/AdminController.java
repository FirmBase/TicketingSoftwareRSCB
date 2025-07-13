package com.rsc.bhopal.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.rsc.bhopal.dtos.NewTicketRate;
import com.rsc.bhopal.dtos.RSCUserDTO;
import com.rsc.bhopal.dtos.ResponseMessage;
import com.rsc.bhopal.dtos.TicketDetailsDTO;
import com.rsc.bhopal.dtos.TicketRate;
import com.rsc.bhopal.dtos.TicketRateByGroup;
import com.rsc.bhopal.dtos.TicketsRatesMasterDTO;
import com.rsc.bhopal.dtos.UserRoleDTO;
import com.rsc.bhopal.dtos.VisitorsTypeDTO;
import com.rsc.bhopal.enums.GroupType;
import com.rsc.bhopal.exception.TicketRateNotMaintainedException;
import com.rsc.bhopal.service.RSCUserDetailsService;
import com.rsc.bhopal.service.TicketDetailsService;
import com.rsc.bhopal.service.TicketsRatesService;
import com.rsc.bhopal.service.VisitorTypeService;
import com.rsc.bhopal.utills.CommonUtills;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/manage")
@Slf4j
public class AdminController {

	@Autowired
	private TicketsRatesService ticketsRatesMasterService;

	@Autowired
	private TicketDetailsService ticketDetailsService;

	@Autowired
	private VisitorTypeService visitorTypeService;

	@Autowired
	private TicketsRatesService ticketsRatesService;

	@Autowired
	private RSCUserDetailsService userDetailsService;

	@GetMapping("/rates")
	public String rates(Map<String, Object> attributes) {
		TicketRateByGroup ticketRateByGroup = new TicketRateByGroup();
		List<VisitorsTypeDTO> vistoryList = visitorTypeService.getVisitorTypeByGroupType(GroupType.SINGLE);
		List<TicketDetailsDTO> tickets = ticketDetailsService.getAllActiveTickets();
		for(TicketDetailsDTO ticket: tickets ){
			Map<Long, Float> prices = new HashMap<Long, Float>();
			TicketRate rate = new TicketRate();
			rate.setTicketId(ticket.getId());
			rate.setTicketName(ticket.getName());

			vistoryList.forEach(visitor -> {
				try{
					TicketsRatesMasterDTO ticketRateMaster = ticketsRatesMasterService.getTicketRateByGroup(ticket.getId(), visitor.getId());
					prices.put(visitor.getId(), ticketRateMaster.getPrice());
				}
				catch(TicketRateNotMaintainedException ex) {
					log.debug(ex.getMessage());
					prices.put(visitor.getId(), null);
					// prices.put(visitor.getId(), 0f);
				}
			});

			rate.setPrices(prices);
			ticketRateByGroup.getTicketRates().add(rate);
		}
		ticketRateByGroup.setGroups(vistoryList.stream().map(VisitorsTypeDTO::getName).collect(Collectors.toList()));
		attributes.put("ticketRates", ticketRateByGroup);
		return "admin/rates";
	}

	@PostMapping("/rates/update")
	public @ResponseBody ResponseEntity<ResponseMessage> updateNewRates(@ModelAttribute NewTicketRate newTicketRate, Principal user) {
		try {
			ticketsRatesService.updateOrAddNewPrice(newTicketRate, user.getName());
			return new ResponseEntity<ResponseMessage>(ResponseMessage.builder()
					.status(true)
					.message("Rate updated to " + newTicketRate.price + ".").build(), HttpStatus.OK);
		}
		catch(Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity<ResponseMessage>(ResponseMessage.builder()
					.status(false)
					.message("Some Error occurred!!\r\nFailed to change price" + newTicketRate.price + ".").build(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/users")
	public String showUsers(Map<String, Object> attributes) {
		List<UserRoleDTO> roles = userDetailsService.getAllRoles();
		List<RSCUserDTO> users = userDetailsService.getAllUser();
		attributes.put("users", users);
		attributes.put("roles", roles);
		return "admin/users";
	}

	@PostMapping("/users/add")
	public String addUsers(@ModelAttribute RSCUserDTO user, final RedirectAttributes redirectAttributes) {
		log.debug(user.toString());
		try {
			userDetailsService.addUser(user);
		}
		catch(InvalidDataAccessApiUsageException ex) {
			log.debug(ex.getMessage());
			addToastMessageOnRedirect(redirectAttributes, "message", "Error adding this user: " + ex.getMessage() + ".", false);
		}
		return "redirect:/manage/users";
	}

	@PostMapping("/users/status")
	public String changeStatus(@RequestParam("username") String username) {
		userDetailsService.changeUserStatus(username);
		return "redirect:/manage/users";
	}

	@PostMapping("/users/password")
	public String changePassword(@RequestParam("username") String username, @RequestParam("password") String password) {
		userDetailsService.changeUserPassword(username, password);
		return "redirect:/manage/users";
	}

	@GetMapping(path = "/users/test")
	public String test(@RequestParam Map<String,String> allRequestParams) {
		//userDetailsService.changeUserStatus(username);
		log.debug(""+allRequestParams.toString());
		return "redirect:/manage/users";
	}

	@GetMapping(path = "/users/test2")
	public String test2(@ModelAttribute TestContainer TestContainer) {
		//userDetailsService.changeUserStatus(username);
		log.debug(""+TestContainer.toString());
		return "redirect:/manage/users";
	}

	private void addToastMessageOnRedirect(final RedirectAttributes redirectAttributes, final String attributeName, final String message, final boolean status) {
		try {
			redirectAttributes.addFlashAttribute(attributeName, CommonUtills.convertToJSON(ResponseMessage.builder().status(status).message(message).build()));
		}
		catch(JsonProcessingException ex_) {
			log.debug("Error: " + ex_.getMessage());
		}
	}
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class Test {
	private Integer id;
	private String name;
	private Boolean checked;
}

@Data
@NoArgsConstructor
@AllArgsConstructor
class TestContainer {
	private ArrayList<Test> tests;
}
