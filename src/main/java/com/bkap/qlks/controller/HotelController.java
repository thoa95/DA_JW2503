package com.bkap.qlks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bkap.qlks.dto.HotelDTO;
import com.bkap.qlks.entity.City;
import com.bkap.qlks.entity.Room;
import com.bkap.qlks.entity.TypeHotel;
import com.bkap.qlks.service.CityService;
import com.bkap.qlks.service.HotelService;
import com.bkap.qlks.service.RoomService;
import com.bkap.qlks.service.TypeHotelService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
	public String getDetailHotel(@PathVariable Long id, Model model) {
		List<HotelDTO> hotelList = hotelService.getAllAsDTO(id);
		if (hotelList.size() > 0) {
			List<Room> roomList = roomService.getRoomsByHotelId(id);
			model.addAttribute("hotel", hotelList.get(0));
			model.addAttribute("roomList", roomList);
			System.out.println(roomList.get(0));
			return "chi-tiet-khach-san";
		}
		return "redirect:/khach-san";
	}
	
//	@GetMapping("/{id}/empty-room")
//    public String getEmptyRoomInHotel(@PathVariable Long id,
//                                   @RequestParam("checkIn") String checkIn,
//                                   @RequestParam("checkOut") String checkOut,
//                                   Model model) {
//        try {
//            LocalDate checkInDate = LocalDate.parse(checkIn);
//            LocalDate checkOutDate = LocalDate.parse(checkOut);
//            
//            List<HotelDTO> hotelList = hotelService.getAllAsDTO(id);
//            if (hotelList.size() > 0) {
//                List<Room> emptyRoomList = roomService.findAvailableRooms(id, checkInDate, checkOutDate);
//                
//                model.addAttribute("khachSan", khachSan.get());
//                model.addAttribute("danhSachPhong", phongTrong);
//                model.addAttribute("ngayDen", ngayDen);
//                model.addAttribute("ngayTra", ngayTra);
//                model.addAttribute("daKiemTraPhongTrong", true);
//                
//                return "chi-tiet-khach-san";
//            }
//        } catch (Exception e) {
//            model.addAttribute("error", "Ngày nhập vào không hợp lệ!");
//        }
//        
//        return "redirect:/khach-san/" + id;
//    }
	

	@GetMapping("/filter")
	@ResponseBody
	public ResponseEntity<List<HotelDTO>> filterKhachSan(@RequestParam(required = false) List<Integer> danhGia,
			@RequestParam(required = false) List<String> loaiKhachSan,
			@RequestParam(required = false) List<Boolean> giapBien, @RequestParam(required = false) String thanhPho) {

		List<HotelDTO> danhSachKhachSan;

		if (thanhPho != null && !thanhPho.trim().isEmpty()) {
			danhSachKhachSan = hotelService.searchByCity(thanhPho);
		} else {
			danhSachKhachSan = hotelService.getAllAsDTO(null);
		}

		List<HotelDTO> ketQua = hotelService.filterKhachSan(danhSachKhachSan, danhGia, loaiKhachSan, giapBien);

		return ResponseEntity.ok(ketQua);
	}

//    @GetMapping("/thanh-pho/{id}")
//    public String khachSanTheoThanhPho(@PathVariable Integer id, Model model) {
//        List<KhachSan> danhSachKhachSan = khachSanService.findByThanhPhoId(id);
//        Optional<ThanhPho> thanhPho = thanhPhoService.findById(id);
//        
//        model.addAttribute("danhSachKhachSan", danhSachKhachSan);
//        model.addAttribute("thanhPho", thanhPho.orElse(null));
//        
//        return "danh-sach-khach-san";
//    }
//    
//    @GetMapping("/loai/{id}")
//    public String khachSanTheoLoai(@PathVariable Integer id, Model model) {
//        List<KhachSan> danhSachKhachSan = khachSanService.findByLoaiKhachSanId(id);
//        Optional<LoaiKhachSan> loaiKhachSan = loaiKhachSanService.findById(id);
//        
//        model.addAttribute("danhSachKhachSan", danhSachKhachSan);
//        model.addAttribute("loaiKhachSan", loaiKhachSan.orElse(null));
//        
//        return "danh-sach-khach-san";
//    }
//    
}