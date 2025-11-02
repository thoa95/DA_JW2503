package com.bkap.qlks.controller;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.bkap.qlks.entity.Account;
//import com.bkap.qlks.service.UserService;



@Controller
public class LoginController {

//	@Autowired
//	private UserService userService;
	
	@GetMapping("/login")
	public String loginUser() {
		return "login";
	}
}
	
