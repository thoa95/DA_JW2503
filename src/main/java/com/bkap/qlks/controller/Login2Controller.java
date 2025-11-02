package com.bkap.qlks.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bkap.qlks.entity.Account;

import jakarta.servlet.http.HttpSession;

@Controller
public class Login2Controller {
	
//	@PostMapping("/login")
//	public String doLogin(@RequestParam String username,
//	                      @RequestParam String password,
//	                      HttpSession session,
//	                      RedirectAttributes redirectAttributes) {
//
//	    Account account = accountService.login(username, password);
//	    if (account == null) {
//	        redirectAttributes.addFlashAttribute("error", "Sai tài khoản hoặc mật khẩu!");
//	        return "redirect:/login";
//	    }
//
//	    session.setAttribute("account", account);
//
//	    // ✅ Nếu có pendingBooking thì quay lại luồng booking
//	    Boolean pending = (Boolean) session.getAttribute("pendingBooking");
//	    if (pending != null && pending) {
//	        session.removeAttribute("pendingBooking");
//	        return "redirect:/booking/confirm";
//	    }
//
//	    return "redirect:/";
//	}

}
