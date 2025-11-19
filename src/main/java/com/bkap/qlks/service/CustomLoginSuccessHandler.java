package com.bkap.qlks.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.bkap.qlks.entity.Account;
import com.bkap.qlks.models.CustomUserDetails;
import com.bkap.qlks.repository.UserRepository;

import java.io.IOException;

@Component
//public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
	public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

//	@Autowired
//    AuthenticationProvider customAuthenticationProvider;
	
	@Autowired
	UserRepository accountRepository;

//    CustomLoginSuccessHandler(AuthenticationProvider customAuthenticationProvider) {
//        this.customAuthenticationProvider = customAuthenticationProvider;
//    }

//	@Override
//    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//                                        Authentication authentication)
//            throws IOException, ServletException {
//
//        HttpSession session = request.getSession();
//        System.out.println("session after: " + session);
//        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
//        Account account = userDetails.getAccount();
//
//        // ✅ Lưu vào session để controller có thể lấy ra
//        session.setAttribute("account", account);
//
//        // ✅ Nếu người dùng đang đặt phòng dở, quay lại trang booking
//        Boolean pendingBooking = (Boolean) session.getAttribute("pendingBooking");
//        System.out.println("pendingBooking: " + pendingBooking);
//        if (pendingBooking != null && pendingBooking) {
//            session.removeAttribute("pendingBooking");
//            response.sendRedirect("/booking/confirm");
//            return;
//        }
//
//        // ✅ Nếu không thì chuyển hướng bình thường theo role
//        if (userDetails.getAuthorities().stream()
//                .anyMatch(a -> a.getAuthority().equals("ADMIN"))) {
//            response.sendRedirect("/admin/home");
//        } else {
//            response.sendRedirect("/");
//        }
//    }

//	 @Override
//	    public void onAuthenticationSuccess(HttpServletRequest request,
//	                                        HttpServletResponse response,
//	                                        Authentication authentication) throws IOException {
//
//	        HttpSession session = request.getSession(true); // tạo session nếu chưa có
//
//	        // Lấy user đã login
//	        String accountId = authentication.getName();
//	        Account account = accountRepository.findByAccountId(accountId);
//
//	        // Lưu vào session
//	        session.setAttribute("account", account);
//
//	        // Kiểm tra pendingBooking
//	        Boolean pendingBooking = (Boolean) session.getAttribute("pendingBooking");
//	        if (Boolean.TRUE.equals(pendingBooking)) {
//	            session.removeAttribute("pendingBooking");
//	            response.sendRedirect("/booking/confirm");
//	            return;
//	        }
//
//	        response.sendRedirect("/"); // login bình thường
//	    }

	
	//popup
	
	 @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {

        HttpSession session = request.getSession(true); // tạo session nếu chưa có

        // Lấy user đã login
        String accountId = authentication.getName();
        Account account = accountRepository.findByAccountId(accountId);

        // Lưu vào session
        session.setAttribute("account", account);

        // Kiểm tra pendingBooking
        Boolean pendingBooking = (Boolean) session.getAttribute("pendingBooking");
        if (Boolean.TRUE.equals(pendingBooking)) {
            session.removeAttribute("pendingBooking");
            response.sendRedirect("/cart?pendingBooking=true"); // redirect về cart với param
            return;
        }

        response.sendRedirect("/"); // login bình thường
    }
}