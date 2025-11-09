package com.bkap.qlks.service;

import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bkap.qlks.entity.Booking;
import com.bkap.qlks.entity.Payment;
import com.bkap.qlks.repository.BookingRepository;
import com.bkap.qlks.repository.PaymentRepository;
import jakarta.transaction.Transactional;

@Service
public class PaymentService {
	@Autowired
	BookingRepository bookingRepository;
	
	@Autowired
	PaymentRepository paymentRepository;
	
	@Transactional
    public void savePaymentStatus(Long bookingId, String paymentMethod) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn đặt phòng với ID: " + bookingId));

        //Cập nhật trạng thái đơn booking
        booking.setPaymentStatus("PAID");
        booking.setUpdateAt(new Date());
        bookingRepository.save(booking);

        //Lưu thông tin thanh toán vào bảng bkap_payment
        Payment payment = new Payment();
        payment.setBookingId(booking.getId());
        payment.setPaymentMethod(paymentMethod);
        payment.setVnpAmount(booking.getTotalAmount());
        payment.setPaymentStatus("SUCCESS");
        payment.setCreatedAt(new Date());
        payment.setDeleteFlg(0);

        paymentRepository.save(payment);
    }
}
