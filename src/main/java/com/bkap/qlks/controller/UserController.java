package com.bkap.qlks.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bkap.qlks.entity.Account;
import com.bkap.qlks.entity.Booking;
import com.bkap.qlks.repository.UserRepository;
import com.bkap.qlks.service.BookingService;

@Controller
public class UserController {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private BookingService bookingService;

	@Autowired
	private BCryptPasswordEncoder passEncoder;

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
		String username = principal.getName();

		List<Booking> bookings = bookingService.getBookingsByUsername(username);
		if (bookings == null) {
			bookings = new ArrayList<>();
		}

		model.addAttribute("bookings", bookings);
		return "lichsudatphong";
	}

	@GetMapping("/booking/cancel/{id}")
	public String cancelBooking(@PathVariable Long id, RedirectAttributes ra) {
		bookingService.cancelBooking(id);
		ra.addFlashAttribute("success", "Hủy đặt phòng thành công!");
		return "redirect:/histo";
	}

	@GetMapping("/change-password")
	public String showChangePassword() {
		return "change-password";
	}

	// Xử lý đổi mật khẩu
	@PostMapping("/change-password")
	public String changePassword(@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword, @RequestParam("confirmPassword") String confirmPassword,
			Principal principal, Model model) {

		// Lấy account hiện tại
		String username = principal.getName();
		Optional<Account> accOpt = userRepository.findById(username);

		if (accOpt.isEmpty()) {
			model.addAttribute("error", "Không tìm thấy tài khoản.");
			return "change-password";
		}

		Account account = accOpt.get();

		// Kiểm tra mật khẩu cũ
		if (!passEncoder.matches(oldPassword, account.getPassword())) {
			model.addAttribute("error", "Mật khẩu cũ không đúng.");
			return "change-password";
		}

		// Kiểm tra trùng khớp
		if (!newPassword.equals(confirmPassword)) {
			model.addAttribute("error", "Mật khẩu mới và xác nhận không trùng khớp.");
			return "change-password";
		}

		account.setPassword(passEncoder.encode(newPassword));
		userRepository.save(account);

		model.addAttribute("success", "Đổi mật khẩu thành công!");
		return "change-password";
	}

	@GetMapping("/edit-profile")
	public String editProfile(Model model, Authentication authentication) {
		if (authentication == null || authentication.getName().equals("anonymousUser")) {
			return "redirect:/login";
		}

		String username = authentication.getName();
		Optional<Account> accOpt = userRepository.findById(username);
		if (accOpt.isEmpty()) {
			return "redirect:/tcn?error=userNotFound";
		}

		model.addAttribute("account", accOpt.get());
		return "edit-profile";
	}

	// --- Cập nhật thông tin người dùng ---
	@PostMapping("/edit-profile")
	public String updateProfile(@ModelAttribute("account") Account updatedAcc, Principal principal) {
		String username = principal.getName();
		Optional<Account> accOpt = userRepository.findById(username);

		if (accOpt.isPresent()) {
			Account existingAcc = accOpt.get();

			// Chỉ cập nhật các thông tin cho phép người dùng thay đổi
			existingAcc.setFull_name(updatedAcc.getFull_name());
			existingAcc.setEmail(updatedAcc.getEmail());
			existingAcc.setPhone(updatedAcc.getPhone());
			existingAcc.setGender(updatedAcc.getGender());

			userRepository.save(existingAcc);
		}

		return "redirect:/tcn?success=updated";
	}

	@GetMapping("/booking/invoice/{id}")
	public ResponseEntity<byte[]> exportInvoice(@PathVariable Long id) {
		byte[] excelData = bookingService.exportInvoiceExcel(id);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		headers.setContentDispositionFormData("attachment", "hoa-don-" + id + ".xlsx");

		return ResponseEntity.ok().headers(headers).body(excelData);
	}

	@GetMapping("/huydatphong")
	public String huydatp() {
		return "redirect:/chitietls";
	}
}
