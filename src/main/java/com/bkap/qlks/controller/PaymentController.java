package com.bkap.qlks.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bkap.qlks.common.Const;
import com.bkap.qlks.config.VNPAYConfig;
import com.bkap.qlks.dto.PaymentDTO;
import com.bkap.qlks.entity.Booking;
import com.bkap.qlks.service.BookingService;
import com.bkap.qlks.service.PaymentService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/payment")
public class PaymentController {
	@Autowired
	PaymentService paymentService;

	@Autowired
	BookingService bookingService;

	@Value("${app.base-url}")
	private static String baseUrl;

	@GetMapping("/start")
	public String startPayment(HttpSession session, Model model, RedirectAttributes redirectAttributes) {
		Long bookingId = (Long) session.getAttribute("bookingId");
		if (bookingId == null) {
			redirectAttributes.addFlashAttribute("error", "Không tìm thấy đơn đặt phòng để thanh toán!");
			return "redirect:/";
		}

		Booking booking = bookingService.getBookingById(bookingId);
		model.addAttribute("booking", booking);
		return "payment/select"; // trang chọn phương thức (VNPay, Momo,...)
	}

	// chọn phương thức thanh toán
	@PostMapping("/choose")
	public String choosePayment(@RequestParam String method, HttpSession session,
			RedirectAttributes redirectAttributes) {
		Long bookingId = (Long) session.getAttribute("bookingId");
		if (bookingId == null) {
			redirectAttributes.addFlashAttribute("error", "Không tìm thấy đơn đặt phòng!");
			return "redirect:/";
		}

		if (Const.VNPAY_METHOD.equalsIgnoreCase(method)) {
			return "redirect:/payment/vnpay/start";
		} else if (Const.CASH_METHOD.equalsIgnoreCase(method)) {
			paymentService.savePaymentStatus(bookingId, Const.CASH_METHOD);
			redirectAttributes.addFlashAttribute("success", "Đặt phòng thành công! Thanh toán bằng tiền mặt.");
			return "redirect:/booking/success";
		}

		redirectAttributes.addFlashAttribute("error", "Phương thức không hợp lệ!");
		return "redirect:/payment/start";
	}

	// thanh toán = VNPAY
	@GetMapping("/vnpay/start")
	public String startVnpay(HttpSession session, RedirectAttributes redirectAttributes) {
		Long bookingId = (Long) session.getAttribute("bookingId");
		if (bookingId == null) {
			redirectAttributes.addFlashAttribute("error", "Không tìm thấy đơn đặt phòng để thanh toán!");
			return "redirect:/";
		}

		// Giả lập thanh toán thành công
		paymentService.savePaymentStatus(bookingId, Const.VNPAY_METHOD);

		redirectAttributes.addFlashAttribute("success", "Thanh toán VNPay thành công!");
		return "redirect:/booking/success";
	}

	@GetMapping("/create")
	@ResponseBody
	public ResponseEntity<?> createPayment(HttpServletRequest req, HttpSession session)
			throws UnsupportedEncodingException {
		String vnp_TmnCode = VNPAYConfig.vnp_TmnCode;

//		Long bookingId = (Long) session.getAttribute("bookingId");
//	    if (bookingId == null) {
//	        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Không tìm thấy bookingId");
//	    }

		String vnp_TxnRef = VNPAYConfig.getRandomNumber(8);
		;
//		String vnp_TxnRef = String.valueOf(bookingId);
		long amount = 1000000 * 100; // 1.000.000 VND
		

		Map<String, String> vnp_Params = new HashMap<>();
		vnp_Params.put("vnp_Version", VNPAYConfig.vnp_Version);
		vnp_Params.put("vnp_Command", VNPAYConfig.vnp_Command);
		vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
		vnp_Params.put("vnp_Amount", String.valueOf(amount));
		vnp_Params.put("vnp_CurrCode", "VND");
		vnp_Params.put("vnp_BankCode", "NCB");
		vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
		vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + vnp_TxnRef);
		vnp_Params.put("vnp_OrderType", "other");
		vnp_Params.put("vnp_Locale", "vn");
		vnp_Params.put("vnp_ReturnUrl", VNPAYConfig.vnp_ReturnUrl);
		vnp_Params.put("vnp_IpAddr", VNPAYConfig.getIpAddress(req));

		Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		String vnp_CreateDate = formatter.format(cld.getTime());
		vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

		cld.add(Calendar.MINUTE, 15);
		String vnp_ExpireDate = formatter.format(cld.getTime());
		vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

		List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
		Collections.sort(fieldNames);
		StringBuilder hashData = new StringBuilder();
		StringBuilder query = new StringBuilder();
		for (Iterator<String> itr = fieldNames.iterator(); itr.hasNext();) {
			String fieldName = itr.next();
			String fieldValue = vnp_Params.get(fieldName);
			if (fieldValue != null && fieldValue.length() > 0) {
				hashData.append(fieldName).append('=')
						.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
				query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8.toString())).append('=')
						.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8.toString()));
				if (itr.hasNext()) {
					hashData.append('&');
					query.append('&');
				}
			}
		}

		String vnp_SecureHash = VNPAYConfig.hmacSHA512(VNPAYConfig.secretKey, hashData.toString());
		String queryUrl = query.toString() + "&vnp_SecureHash=" + vnp_SecureHash;
		String paymentUrl = VNPAYConfig.vnp_PayUrl + "?" + queryUrl;

		PaymentDTO paymentDTO = new PaymentDTO();
		paymentDTO.setStatus("Ok");
		paymentDTO.setMessage("Success");
		paymentDTO.setURL(paymentUrl);
		return ResponseEntity.status(HttpStatus.OK).body(paymentDTO);
	}

//	@GetMapping("/info")
//	@ResponseBody
//	public ResponseEntity<?> transaction(
//			@RequestParam(value = "vnp_Amount", required = false) String amount, 
//			@RequestParam(value = "vnp_BankCode", required = false) String bankCode, 
//			@RequestParam(value = "vnp_OrderInfo", required = false) String order, 
//			@RequestParam(value = "vnp_ResponseCode", required = false) String responseCode,
//			@RequestParam(value = "vnp_TxnRef", required = false) String vnp_TxnRef
//	) {
//		if ("00".equals(responseCode)) {
//	        Long bookingId = Long.valueOf(vnp_TxnRef);
////	        paymentService.savePaymentStatus(bookingId, Const.VNPAY_METHOD);
//
//	        return ResponseEntity.ok("Thanh toán thành công cho đơn " + bookingId + 
//	            ", số tiền: " + amount);
//	    } else {
//	        return ResponseEntity.ok("Thanh toán thất bại. Mã lỗi: " + responseCode);
//	    }
//	}

	@GetMapping("/info")
	public String transactionRedirect(@RequestParam(value = "vnp_Amount", required = false) String amount,
			@RequestParam(value = "vnp_BankCode", required = false) String bankCode,
			@RequestParam(value = "vnp_OrderInfo", required = false) String order,
			@RequestParam(value = "vnp_ResponseCode", required = false) String responseCode,
			@RequestParam(value = "vnp_TxnRef", required = false) String vnp_TxnRef,
			RedirectAttributes redirectAttributes) {
		if ("00".equals(responseCode)) {
			// Thanh toán thành công
			Long bookingId = Long.valueOf(vnp_TxnRef);
			// paymentService.savePaymentStatus(bookingId, Const.VNPAY_METHOD); // nếu cần
			// lưu DB

			// Truyền dữ liệu sang view thành công
			redirectAttributes.addFlashAttribute("status", "success");
			redirectAttributes.addFlashAttribute("message", "Thanh toán thành công cho đơn " + bookingId);
			redirectAttributes.addFlashAttribute("amount", amount);

			return "redirect:/booking/success"; // trang success
		} else {
			// Thanh toán thất bại
			redirectAttributes.addFlashAttribute("status", "fail");
			redirectAttributes.addFlashAttribute("message", "Thanh toán thất bại. Mã lỗi: " + responseCode);
			redirectAttributes.addFlashAttribute("amount", amount);

			return "redirect:/booking/fail"; // trang fail
		}
	}

}