package com.rsc.bhopal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
	// @GetMapping("/")
	// public String loginPage2() {
	// 	return "login";
	// }

	@GetMapping("/login")
	public String loginPage() {
		return "login";
	}
}
