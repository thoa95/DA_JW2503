package com.bkap.qlks.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bkap.qlks.dto.CartItem;
import com.bkap.qlks.entity.Account;
import com.bkap.qlks.entity.Booking;
import com.bkap.qlks.entity.BookingRoom;
import com.bkap.qlks.repository.BookingRepository;
import com.bkap.qlks.repository.BookingRoomRepository;

@Service
public class BookingService {

	@Autowired
	private BookingRepository bookingRepository;

	@Autowired
	private BookingRoomRepository bookingRoomRepository;

	public Booking getBookingById(Long bookingId) {
		return bookingRepository.findById(bookingId)
				.orElseThrow(() -> new RuntimeException("Không tìm thấy đơn đặt phòng với ID: " + bookingId));
	}

	public Booking createBooking(Account account, List<CartItem> cartItems) {
		// Tính tổng tiền
		Integer total = cartItems.size() == 0 ? 0
				: cartItems.stream().mapToInt(item -> (int) item.getPrice() * item.getNumberDay()).sum();

		// Tạo booking chính
		Booking booking = new Booking();
		booking.setAccountId(account.getAccountId());
		booking.setTotalAmount(total);
		booking.setPaymentStatus("UNPAID");
		booking.setIsCancel(0);
		booking.setDeleteFlg(0);
		booking.setCreatedAt(new Date());
		booking.setUpdateAt(new Date());
		booking = bookingRepository.save(booking);

		List<BookingRoom> bookingRoomList = new ArrayList<>();

		// Lưu chi tiết từng phòng
		for (CartItem item : cartItems) {

			Date checkIn = new Date();
			Date checkOut = new Date();

			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				checkIn = sdf.parse(item.getFromDate());
				checkOut = sdf.parse(item.getToDate());
			} catch (ParseException e) {
				e.printStackTrace();
			}

			BookingRoom br = new BookingRoom();
			br.getBookingRoomId().setBookingId(booking.getId());
			br.getBookingRoomId().setRoomId(item.getRoomId());
			br.setCheckInDate(checkIn);
			br.setCheckOutDate(checkOut);
			br.setPrice(item.getPrice());
			br.setAccountId(account.getAccountId());
			br.setCreatedAt(new Date());
			br.setUpdateAt(new Date());
			br.setDeleteFlg(0);
			bookingRoomList.add(br);

		}
		if (bookingRoomList.size() > 0) {
			bookingRoomRepository.saveAll(bookingRoomList);
		}

		return booking;
	}
}
