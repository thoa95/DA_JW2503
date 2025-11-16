package com.bkap.qlks.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@GetMapping({ "", "/" })
	public String ad() {
		return "admin/admin";
	}
	@GetMapping("/moi")
	public String cre() {
		return "admin/create";
	}
}
