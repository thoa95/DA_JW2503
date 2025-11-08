package com.bkap.qlks.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.bkap.qlks.entity.Account;
import com.bkap.qlks.models.CustomUserDetails;

import java.io.IOException;

@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

	@Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {

        HttpSession session = request.getSession();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Account account = userDetails.getAccount();

        // ✅ Lưu vào session để controller có thể lấy ra
        session.setAttribute("account", account);

        // ✅ Nếu người dùng đang đặt phòng dở, quay lại trang booking
        Boolean pendingBooking = (Boolean) session.getAttribute("pendingBooking");
        if (pendingBooking != null && pendingBooking) {
            session.removeAttribute("pendingBooking");
            response.sendRedirect("/booking/start");
            return;
        }

        // ✅ Nếu không thì chuyển hướng bình thường theo role
        if (userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
            response.sendRedirect("/admin/home");
        } else {
            response.sendRedirect("/");
        }
    }
}