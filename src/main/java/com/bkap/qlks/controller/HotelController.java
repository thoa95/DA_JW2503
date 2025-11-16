package com.bkap.qlks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bkap.qlks.common.Const;
import com.bkap.qlks.dto.HotelDTO;
import com.bkap.qlks.entity.City;
import com.bkap.qlks.entity.Room;
import com.bkap.qlks.entity.TypeHotel;
import com.bkap.qlks.service.CityService;
import com.bkap.qlks.service.HotelService;
import com.bkap.qlks.service.RoomService;
import com.bkap.qlks.service.TypeHotelService;

import java.util.List;

@Controller
@RequestMapping("/hotel")
public class HotelController {

	@Autowired
	private HotelService hotelService;

	@Autowired
	private CityService cityService;

	@Autowired
	private TypeHotelService typeHotelService;

	@Autowired
	private RoomService roomService;

	@GetMapping
	public String getListHotel(Model model) {
		List<TypeHotel> typeHotelList = typeHotelService.getAll();
		List<HotelDTO> hotelList = hotelService.getAllAsDTO(null);
		List<City> cityList = cityService.getAll();

		model.addAttribute("hotelList", hotelList);
		model.addAttribute("typeHotelList", typeHotelList);
		model.addAttribute("cityList", cityList);
		return "danh-sach-khach-san";
	}

	@GetMapping("/{id}")
	public String getDetailHotel(@PathVariable(name = "id") Long id, @RequestParam(name = "page", defaultValue = "0") int page, Model model) {
		List<HotelDTO> hotelList = hotelService.getAllAsDTO(id);
		if (hotelList.size() > 0) {
			Page<Room> roomPage = roomService.getRoomsByHotelId(id, page, Const.size);
			model.addAttribute("hotel", hotelList.get(0));
			model.addAttribute("roomPage", roomPage);
			model.addAttribute("roomList", roomPage.getContent());
			return "chi-tiet-khach-san2";
		}
		return "redirect:/hotel";
	}

	@GetMapping("/{id}/empty-room")
	public String getEmptyRoomInHotel(@PathVariable(name = "id") Long id,
									@RequestParam(name = "fromDate") String fromDate,
									@RequestParam(name = "toDate") String toDate,
									@RequestParam(name = "page", defaultValue = "0") int page,
									Model model) {
		try {
				
			List<HotelDTO> hotelList = hotelService.getAllAsDTO(id);
			if (hotelList.size() > 0) {
				Page<Room> roomPage = roomService.findAvailableRooms(id, fromDate, toDate, page, Const.size);
				model.addAttribute("hotel", hotelList.get(0));
				model.addAttribute("roomPage", roomPage);
				model.addAttribute("roomList", roomPage.getContent());
				model.addAttribute("fromDate", fromDate);
				model.addAttribute("toDate", toDate);
				model.addAttribute("checkedRooms", true);
				return "chi-tiet-khach-san2";
			}
		} catch (Exception e) {
			model.addAttribute("error", "Ngày nhập vào không hợp lệ!");
		}

		return "redirect:/hotel/" + id;
	}

	@GetMapping("/filter")
	@ResponseBody
	public ResponseEntity<List<HotelDTO>> filterKhachSan(
			@RequestParam(name = "evaluateList",required = false) List<Integer> evaluateList,
			@RequestParam(name = "typeHotelList",required = false) List<Long> typeHotelList,
			@RequestParam(name = "nearSea",required = false) Integer nearSea,
			@RequestParam(name = "cityList",required = false) List<Long> cityList) {

		List<HotelDTO> result = hotelService.searchHotel(evaluateList, typeHotelList, nearSea, cityList);
		return ResponseEntity.ok(result);
	}
	
	
	@GetMapping("/city/{id}")
	public String getListHotelByCity(@PathVariable(name = "id", required = false) Long id, Model model) {
		List<TypeHotel> typeHotelList = typeHotelService.getAll();
		List<City> cityList = cityService.getAll();

		model.addAttribute("cityList", cityList);
		model.addAttribute("typeHotelList", typeHotelList);
		model.addAttribute("selectedCity", id);
		
		return "danh-sach-khach-san";
	}
    
    
    
    @GetMapping("/type/{id}")
    public String getListHotelByType(@PathVariable(name = "id",required = false) Long id, Model model) {
    	List<TypeHotel> typeHotelList = typeHotelService.getAll();
		List<City> cityList = cityService.getAll();

		model.addAttribute("cityList", cityList);
		model.addAttribute("typeHotelList", typeHotelList);
		model.addAttribute("selectedType", id);
		
		return "danh-sach-khach-san";
    }
    
}