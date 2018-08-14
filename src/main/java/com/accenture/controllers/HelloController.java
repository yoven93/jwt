package com.accenture.controllers;

import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/hello")
public class HelloController {
	
	@Secured("ROLE_ADMIN")
	@GetMapping("/admin")
	public String adminSayHello() {
		return "Hello Admin";
	}
	
	@Secured("ROLE_USER")
	@GetMapping("/user")
	public String userSayHello() {
		return "Hello User";
	}
}
