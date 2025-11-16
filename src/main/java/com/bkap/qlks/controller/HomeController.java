package com.bkap.qlks.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.bkap.qlks.repository.HotelRepository;
import com.bkap.qlks.service.CityService;
import com.bkap.qlks.service.HotelService;
import com.bkap.qlks.service.TypeHotelService;

@Controller
public class HomeController {
	@Autowired
	CityService cityService;

	@Autowired
	HotelService hotelService;
	
	@Autowired
	HotelRepository hotelRepository;

	@Autowired
	TypeHotelService typeHotelService;

	@GetMapping("/")
	public String home(Model model) {
		model.addAttribute("tenThanhPhoTimKiem", "");
		model.addAttribute("thoiGianTimKiem", "");
		model.addAttribute("listLoaiKhachSan", typeHotelService.getAll());
		model.addAttribute("listKhachSan", hotelRepository.findAllByCity(null));
		model.addAttribute("listThanhPho", cityService.getAll());

		model.addAttribute("strDanhGia", Map.of(1, "Tệ", 2, "Bình thường", 3, "Khá", 4, "Rất tốt", 5, "Xuất sắc"));

		return "index";
	}
}
