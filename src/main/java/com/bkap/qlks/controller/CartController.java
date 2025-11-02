package com.bkap.qlks.controller;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bkap.qlks.dto.CartItem;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/cart")
public class CartController {
	
	@GetMapping
	public String viewCart(HttpSession session, org.springframework.ui.Model model) {
		@SuppressWarnings("unchecked")
		List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("totalPrice", cartItems == null ? 0
				: cartItems.stream().mapToLong(item -> (long) item.getPrice() * item.getNumberDay()).sum());
		return "cart";
	}
	

	@SuppressWarnings("unchecked")
	@PostMapping("/add")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> addToCart(
			@RequestParam Long roomId,
			@RequestParam String roomName,
			@RequestParam Integer price,
			@RequestParam String fromDate,
			@RequestParam String toDate,
			HttpSession session) {

		List<CartItem> cart = (List<CartItem>) session.getAttribute("cartItems");
		if (cart == null) {
			cart = new ArrayList<>();
		}

		boolean exists = cart.stream().anyMatch(item -> item.getRoomId().equals(roomId));
		if (exists) {
			return ResponseEntity.badRequest().body(Map.of("error", "Phòng này đã có trong giỏ hàng."));
		}

		CartItem newItem = new CartItem(roomId, roomName, price, fromDate, toDate);
		cart.add(newItem);
		
		session.setAttribute("cartItems", cart);
		return ResponseEntity.ok(Map.of("message", "Đã thêm phòng vào giỏ!", "cartCount", cart.size()));
	}



	@PostMapping("/update")
	public String updateCartItem(@RequestParam Long roomId, @RequestParam String fromDate, @RequestParam String toDate,
			HttpSession session, RedirectAttributes redirectAttributes) {

		@SuppressWarnings("unchecked")
		List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");

		if (cartItems == null || cartItems.isEmpty()) {
			redirectAttributes.addFlashAttribute("error", "Giỏ hàng hiện trống hoặc đã hết phiên!");
			return "redirect:/cart";
		}

		try {
			LocalDate from = LocalDate.parse(fromDate);
			LocalDate to = LocalDate.parse(toDate);

			if (to.isBefore(from)) {
				redirectAttributes.addFlashAttribute("error", "❌ Ngày trả phải sau hoặc bằng ngày nhận!");
				return "redirect:/cart";
			}

		} catch (DateTimeParseException e) {
			redirectAttributes.addFlashAttribute("error", "❌ Ngày không hợp lệ!");
			return "redirect:/cart";
		}

		boolean found = false;

		for (CartItem item : cartItems) {
			if (item.getRoomId().equals(roomId)) {
				item.setFromDate(fromDate);
				item.setToDate(toDate);
				item.setNumberDay(CartItem.calculateNumberDay(fromDate, toDate));
				found = true;
				break;
			}
		}

		if (!found) {
			redirectAttributes.addFlashAttribute("error", "Không tìm thấy phòng trong giỏ hàng!");
			return "redirect:/cart";
		}

		long totalPrice = cartItems.size() == 0 ? 0
				: cartItems.stream().mapToLong(item -> (long) item.getPrice() * item.getNumberDay()).sum();

		session.setAttribute("cartItems", cartItems);
		session.setAttribute("totalPrice", totalPrice);
		redirectAttributes.addFlashAttribute("success", "Cập nhật thành công!");
		return "redirect:/cart";
	}

	
	
	@GetMapping("/count")
	@ResponseBody
	public ResponseEntity<Integer> getCartCount(HttpSession session) {
		@SuppressWarnings("unchecked")
		List<CartItem> cart = (List<CartItem>) session.getAttribute("cartItems");
		int count = (cart == null) ? 0 : cart.size();
		return ResponseEntity.ok(count);
	}

	
	
	// Xóa 1 phòng
	@GetMapping("/remove/{id}")
	public String removeItem(@PathVariable("id") Long roomId, HttpSession session,
			RedirectAttributes redirectAttributes) {

		@SuppressWarnings("unchecked")
		List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");

		if (cartItems != null) {
			cartItems.removeIf(item -> item.getRoomId().equals(roomId));
			session.setAttribute("cartItems", cartItems);
		}

		redirectAttributes.addFlashAttribute("success", "Đã xóa phòng khỏi giỏ!");
		return "redirect:/cart";
	}

	
	
	// Xóa toàn bộ giỏ hàng
	@GetMapping("/clear")
	public String clearCart(HttpSession session, RedirectAttributes redirectAttributes) {
		session.removeAttribute("cartItems");
		redirectAttributes.addFlashAttribute("success", "Đã xóa toàn bộ giỏ hàng!");
		return "redirect:/cart";
	}
}
