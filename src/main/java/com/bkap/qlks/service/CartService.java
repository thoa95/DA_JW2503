package com.bkap.qlks.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.bkap.qlks.dto.CartItem;

import jakarta.servlet.http.HttpSession;

@Service
public class CartService {

	    @SuppressWarnings("unchecked")
		public List<CartItem> getCart(HttpSession session) {
	        return (List<CartItem>) session.getAttribute("cartItems");
	    }

	    public void clearCart(HttpSession session) {
	        session.removeAttribute("cartItems");
	    }

}
