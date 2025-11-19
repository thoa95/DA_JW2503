package com.bkap.qlks.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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

	public List<Booking> getBookingsByUsername(String username) {
		return bookingRepository.findByAccountIdOrderByCreatedAtDesc(username);
	}

	public void cancelBooking(Long id) {
	    Booking booking = getBookingById(id);
	    if (booking != null) {

	        // 1) Đánh dấu đã hủy
	        booking.setIsCancel(1);

	        // 2) Không được đổi payment_status sang tiếng Việt (sai CHECK)
	        // Nếu muốn đánh dấu hủy thì chỉ giữ UNPAID hoặc PAID
	        if (!"PAID".equals(booking.getPaymentStatus())) {
	            booking.setPaymentStatus("UNPAID");
	        }

	        booking.setUpdateAt(new Date());
	        bookingRepository.save(booking);
	    }
	}
	public byte[] exportInvoiceExcel(Long bookingId) {
	    Booking booking = getBookingById(bookingId);

	    try (Workbook workbook = new XSSFWorkbook()) {
	        Sheet sheet = workbook.createSheet("Invoice");

	        // STYLE BOLD
	        CellStyle headerStyle = workbook.createCellStyle();
	        Font font = workbook.createFont();
	        font.setBold(true);
	        headerStyle.setFont(font);

	        int rowIdx = 0;

	        // --- HEADER ---
	        Row header = sheet.createRow(rowIdx++);
	        header.createCell(0).setCellValue("HÓA ĐƠN ĐẶT PHÒNG");
	        header.getCell(0).setCellStyle(headerStyle);

	        rowIdx++; // dòng trống

	        // --- INFO ---
	        Row r1 = sheet.createRow(rowIdx++);
	        r1.createCell(0).setCellValue("Mã đặt phòng:");
	        r1.createCell(1).setCellValue(booking.getId());

	        Row r2 = sheet.createRow(rowIdx++);
	        r2.createCell(0).setCellValue("Khách hàng:");
	        r2.createCell(1).setCellValue(booking.getAccountId());

	        Row r3 = sheet.createRow(rowIdx++);
	        r3.createCell(0).setCellValue("Ngày đặt:");
	        r3.createCell(1).setCellValue(booking.getCreatedAt().toString());

	        Row r4 = sheet.createRow(rowIdx++);
	        r4.createCell(0).setCellValue("Tổng tiền:");
	        r4.createCell(1).setCellValue(booking.getTotalAmount());

	        Row r5 = sheet.createRow(rowIdx++);
	        r5.createCell(0).setCellValue("Trạng thái:");
	        r5.createCell(1).setCellValue(
	                booking.getIsCancel() == 1 ? "Đã hủy" :
	                        (booking.getPaymentStatus().equals("PAID") ? "Đã thanh toán" : "Chưa thanh toán")
	        );

	        // Xuất ra byte[]
	        ByteArrayOutputStream output = new ByteArrayOutputStream();
	        workbook.write(output);
	        return output.toByteArray();

	    } catch (IOException e) {
	        throw new RuntimeException("Lỗi xuất file Excel", e);
	    }
	}

}
