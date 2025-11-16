package com.bkap.qlks.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bkap.qlks.common.Const;
import com.bkap.qlks.dto.CartItem;
import com.bkap.qlks.dto.HotelDTO;
import com.bkap.qlks.entity.Room;
import com.bkap.qlks.repository.HotelRepository;
import com.bkap.qlks.repository.RoomRepository;
import com.bkap.qlks.service.CityService;
import com.bkap.qlks.service.RoomService;

import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/room")
public class RoomController {

	@Autowired
	private RoomService roomService;
	
	@Autowired
	private RoomRepository roomRepository;
	
	@Autowired
	private HotelRepository hotelRepository;

	@Autowired
	CityService cityService;

	@RequestMapping(value = "/search", method = {RequestMethod.GET, RequestMethod.POST})
	public String searchRooms(
	        @RequestParam(required = false) Long cityId,
	        @RequestParam(required = false) String checkIn,
	        @RequestParam(required = false) String checkOut,
	        @RequestParam(required = false) String bed,
	        @RequestParam(defaultValue = "0") int page,
	        @RequestParam(required = false) String fromPage, // "index" hoặc "danh-sach"
	        Model model) {

	    Integer bedNumber = null;
	    if (bed != null && !bed.trim().isEmpty()) {
	        try {
	            bedNumber = Integer.parseInt(bed);
	        } catch (NumberFormatException e) {
	            bedNumber = null;
	        }
	    }

	    Page<Room> roomPage = roomService.searchQuick(cityId, checkIn, checkOut, bedNumber, page, Const.size);

	    model.addAttribute("roomPage", roomPage);
	    model.addAttribute("roomList", roomPage.getContent());
	    model.addAttribute("fromDate", checkIn);
	    model.addAttribute("toDate", checkOut);
	    model.addAttribute("cityId", cityId);
	    model.addAttribute("bed", bedNumber);
	    model.addAttribute("page", page);
	    model.addAttribute("listThanhPho", cityService.getAll());
	    model.addAttribute("checkedRooms", checkIn != null && checkOut != null);

	    if (roomPage.getContent().isEmpty()) {
	        model.addAttribute("error", "Không tìm thấy phòng khả dụng. Vui lòng chọn lại!");
	        // nếu đang từ trang chủ, trả về index
	        if ("index".equals(fromPage)) {
	            return "index";
	        }
	        // nếu đang ở trang danh sách, vẫn trả về danh-sach-phong
	        return "danh-sach-phong";
	    }

	    // Nếu có phòng và từ trang chủ → chuyển sang trang danh sách
	    if ("index".equals(fromPage)) {
	        return "danh-sach-phong";
	    }

	    // Nếu từ trang danh sách → ở lại
	    return "danh-sach-phong";
	}

	
	@GetMapping("/detail/{id}")
	public String roomDetail(@PathVariable Long id,
	        @RequestParam(required = false) String fromDate,
	        @RequestParam(required = false) String toDate,
	        @RequestParam(required = false, defaultValue = "0") int page,
	        Model model) {
		List<HotelDTO> hotelDTOs = hotelRepository.findHotelByRoom( id);
	    Room room = roomRepository.findById(id).orElseThrow(() -> new RuntimeException("Room not found"));

	    model.addAttribute("room", room);
	    model.addAttribute("hotel", (!hotelDTOs.isEmpty()) ? hotelDTOs.get(0) : new HotelDTO());
	    model.addAttribute("fromDate", fromDate);
	    model.addAttribute("toDate", toDate);
	    model.addAttribute("page", page);
	    return "room-detail";
	}

	@PostMapping("/check-availability")
	@ResponseBody
	public List<Map<String, Object>> checkAvailability(HttpSession session) {
	    // Lấy giỏ booking từ session
	    List<CartItem> cartItems = (List<CartItem>) session.getAttribute("cartItems");

	    List<Map<String, Object>> result = new ArrayList<>();

	    if(cartItems != null) {
	        for(CartItem item : cartItems) {
	            Boolean available = (roomService.checkEmptyRoom(
	                    item.getRoomId(),
	                    item.getFromDate(),
	                    item.getToDate()
	            )) != null ? true : false;

	            Map<String, Object> map = new HashMap<>();
	            map.put("roomId", item.getRoomId());
	            map.put("available", available);
	            result.add(map);
	        }
	    }

	    return result; // JSON: [{roomId:1, available:true}, ...]
	}



}