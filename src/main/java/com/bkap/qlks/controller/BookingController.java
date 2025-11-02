package com.bkap.qlks.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bkap.qlks.dto.CartItem;
import com.bkap.qlks.entity.Account;
import com.bkap.qlks.entity.Booking;
import com.bkap.qlks.service.BookingService;
import com.bkap.qlks.service.CartService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/booking")
public class BookingController {
	@Autowired
	private CartService cartService;
	
	@Autowired
	private BookingService bookingService;
	
	@GetMapping("/start")
    public String startBooking(HttpSession session, RedirectAttributes redirectAttributes) {
        // ✅ Kiểm tra đăng nhập
        Account account = (Account) session.getAttribute("account");
        if (account == null) {
            // Ghi nhớ là người dùng đang đặt phòng
            session.setAttribute("pendingBooking", true);
            redirectAttributes.addFlashAttribute("info", "Vui lòng đăng nhập để tiếp tục đặt phòng!");
            return "redirect:/login";
        }

        return "redirect:/booking/confirm";
    }
	
	@PostMapping("/confirm")
	public String confirmBooking(HttpSession session, RedirectAttributes redirectAttributes) {
	    Account account = (Account) session.getAttribute("account");
	    if (account == null) {
	        redirectAttributes.addFlashAttribute("error", "Phiên đăng nhập đã hết hạn!");
	        return "redirect:/login";
	    }

	    List<CartItem> cartItems = cartService.getCart(session);
	    if (cartItems == null || cartItems.isEmpty()) {
	        redirectAttributes.addFlashAttribute("error", "Giỏ hàng trống!");
	        return "redirect:/cart";
	    }

	    Booking booking = bookingService.createBooking(account, cartItems);
	    session.setAttribute("bookingId", booking.getId());
	    cartService.clearCart(session);

	    return "redirect:/payment/start";
	}
	
	
	   @GetMapping("/success")
	    public String bookingSuccess(HttpSession session, Model model) {
	        Long bookingId = (Long) session.getAttribute("bookingId");
	        if (bookingId != null) {
	            Booking booking = bookingService.getBookingById(bookingId);
	            model.addAttribute("booking", booking);
	        }
	        session.removeAttribute("bookingId"); // Xoá session tránh double submit
	        return "booking/success";
	    }

}