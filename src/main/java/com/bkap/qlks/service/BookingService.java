package com.bkap.qlks.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.bkap.qlks.entity.Room;
import com.bkap.qlks.repository.BookingRepository;
import com.bkap.qlks.repository.BookingRoomRepository;
import com.bkap.qlks.repository.RoomRepository;

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
	    if (booking == null) return;

	    List<BookingRoom> rooms = bookingRoomRepository.findByBookingRoomId_BookingId(id);
	    Date now = new Date();

	    // 1️⃣ KIỂM TRA QUÁ HẠN CHECKOUT → TỰ HỦY
	    boolean isOverdue = rooms.stream()
	            .anyMatch(r -> r.getCheckOutDate().before(now));

	    if (isOverdue) {

	        // Nếu quá hạn checkout mà chưa thanh toán → tự động hủy
	        if (!"PAID".equals(booking.getPaymentStatus())) {
	            booking.setIsCancel(1);
	            booking.setUpdateAt(now);
	            bookingRepository.save(booking);
	        }

	        // Nếu quá hạn và đã thanh toán → không thể hủy
	        throw new RuntimeException("Đơn đặt phòng đã hoàn tất. Không thể hủy nữa.");
	    }

	    // 2️⃣ NẾU CHƯA QUÁ HẠN → CHO HỦY THỦ CÔNG
	    booking.setIsCancel(1);

	    // Nếu chưa thanh toán giữ nguyên UNPAID
	    if (!"PAID".equals(booking.getPaymentStatus())) {
	        booking.setPaymentStatus("UNPAID");
	    }

	    booking.setUpdateAt(now);
	    bookingRepository.save(booking);
	}

	@Autowired
	private RoomRepository roomRepo;

	public byte[] exportInvoiceExcel(Long bookingId) {
	    Booking booking = getBookingById(bookingId);
	    List<BookingRoom> rooms = bookingRoomRepository.findByBookingRoomId_BookingId(bookingId);

	    try (Workbook workbook = new XSSFWorkbook()) {

	        Sheet sheet = workbook.createSheet("Invoice");
	        int rowIdx = 0;

	        // HEADER
	        Row title = sheet.createRow(rowIdx++);
	        title.createCell(0).setCellValue("HÓA ĐƠN ĐẶT PHÒNG - BORCELLE HOTEL");

	        rowIdx++;

	        // BOOKING INFO
	        sheet.createRow(rowIdx++).createCell(0).setCellValue("Mã đặt phòng: " + booking.getId());
	        sheet.createRow(rowIdx++).createCell(0).setCellValue("Khách hàng: " + booking.getAccountId());
	        sheet.createRow(rowIdx++).createCell(0).setCellValue("Ngày đặt: " + booking.getCreatedAt());
	        sheet.createRow(rowIdx++).createCell(0).setCellValue("Tổng tiền: " + booking.getTotalAmount());
	        sheet.createRow(rowIdx++).createCell(0).setCellValue(
	                "Trạng thái: " + (booking.getIsCancel() == 1 ? "Đã hủy"
	                        : (booking.getPaymentStatus().equals("PAID") ? "Đã thanh toán" : "Chưa thanh toán"))
	        );

	        rowIdx += 2;

	        // ROOM TABLE HEADER
	        Row header = sheet.createRow(rowIdx++);
	        String[] cols = {"Tên phòng", "Diện tích", "Giường", "Giá", "Check-in", "Check-out", "Tiện nghi"};
	        for (int i = 0; i < cols.length; i++) header.createCell(i).setCellValue(cols[i]);

	        // ROOM LIST
	        for (BookingRoom br : rooms) {
	            Room room = roomRepo.findById(br.getBookingRoomId().getRoomId()).orElse(null);

	            Row row = sheet.createRow(rowIdx++);

	            row.createCell(0).setCellValue(room != null ? room.getName() : "N/A");
	            row.createCell(1).setCellValue(room != null ? room.getArea() : 0);
	            row.createCell(2).setCellValue(room != null ? room.getBed() : 0);
	            row.createCell(3).setCellValue(br.getPrice());
	            row.createCell(4).setCellValue(br.getCheckInDate().toString());
	            row.createCell(5).setCellValue(br.getCheckOutDate().toString());

	            if (room != null && room.getAmenities() != null) {
	                String amenities = room.getAmenities().stream()
	                        .map(a -> a.getName())
	                        .reduce((a, b) -> a + ", " + b).orElse("");
	                row.createCell(6).setCellValue(amenities);
	            } else {
	                row.createCell(6).setCellValue("Không có");
	            }
	        }

	        ByteArrayOutputStream output = new ByteArrayOutputStream();
	        workbook.write(output);
	        return output.toByteArray();
	    } catch (Exception e) {
	        throw new RuntimeException("Lỗi export Excel", e);
	    }
	}

	public Map<Long, List<BookingRoom>> getBookingRoomsMap(List<Booking> list) {
	    Map<Long, List<BookingRoom>> map = new HashMap<>();

	    for (Booking b : list) {
	        List<BookingRoom> rooms = bookingRoomRepository.findByBookingRoomId_BookingId(b.getId());
	        map.put(b.getId(), rooms);
	    }

	    return map;
	}


}
