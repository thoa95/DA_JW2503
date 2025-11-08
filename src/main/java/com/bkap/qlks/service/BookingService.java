package com.bkap.qlks.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bkap.qlks.entity.Booking;
import com.bkap.qlks.repository.BookingRepository;

@Service
public class BookingService {
    @Autowired
    private BookingRepository bookingRepository;

    public List<Booking> getBookingsByUsername(String username) {
        return bookingRepository.findByAccountIdOrderByCreatedAtDesc(username);
    }
    
    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }

    public void cancelBooking(Long id) {
        Booking booking = getBookingById(id);
        if (booking != null) {
            booking.setIsCancel(1);
            booking.setPaymentStatus("Đã hủy");
            bookingRepository.save(booking);
        }
    }

}
