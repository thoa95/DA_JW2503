package com.bkap.qlks.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.bkap.qlks.entity.Account;
import com.bkap.qlks.entity.Booking;
import com.bkap.qlks.entity.News;
import com.bkap.qlks.repository.UserRepository;
import com.bkap.qlks.service.BookingService;

@Controller
public class UserController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BookingService bookingService;

	@GetMapping("/tcn")
	public String tcn(Model model, Authentication authentication) {
		if (authentication == null || authentication.getName().equals("anonymousUser")) {
			return "redirect:/login"; // Nếu chưa đăng nhập
		}

		// Lấy accountId từ người dùng đăng nhập hiện tại
		String currentUserId = authentication.getName();

		// Tìm thông tin người dùng trong DB
		Optional<Account> accountOp = userRepository.findById(currentUserId);
		if (accountOp.isPresent()) {
			Account account = accountOp.get();

			// Truyền account sang view
			model.addAttribute("account", account);
			return "trangcanhan";
		} else {
			return "redirect:/login?error=userNotFound";
		}
	}

	@GetMapping("/histo")
	public String histo(Model model, Principal principal) {
		// Lấy username từ người đăng nhập
		String username = principal.getName();

		// Lấy danh sách đặt phòng của user
		List<Booking> bookings = bookingService.getBookingsByUsername(username);

		// Nếu null thì trả về list rỗng
		if (bookings == null) {
			bookings = new ArrayList<>();
		}

		// Đưa vào model
		model.addAttribute("bookings", bookings);
		return "lichsudatphong";

	}

	@GetMapping("/change-password")
	public String changepass() {

		return "change-password";
	}

	@GetMapping("/edit-profile")
	public String editpro() {

		return "edit-profile";
	}

}