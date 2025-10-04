package com.bkap.qlks.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bkap.qlks.dto.HotelDTO;
import com.bkap.qlks.entity.TypeHotel;
import com.bkap.qlks.service.CityService;
import com.bkap.qlks.service.HotelService;
import com.bkap.qlks.service.TypeHotelService;

import java.util.List;

@Controller
@RequestMapping("/khach-san")
public class HotelController {

	@Autowired
	private HotelService hotelService;

	@Autowired
	private CityService cityService;

	@Autowired
	private TypeHotelService typeHotelService;

	@GetMapping
	public String danhSachKhachSan(Model model) {
		List<TypeHotel> typeHotelList = typeHotelService.getAll();
		List<HotelDTO> hotelList = hotelService.getAllAsDTO();

		model.addAttribute("hotelList", hotelList);
		model.addAttribute("typeHotelList", typeHotelList);

		return "danh-sach-khach-san";
	}

	@GetMapping("/filter")
	@ResponseBody
	public ResponseEntity<List<HotelDTO>> filterKhachSan(
						@RequestParam(required = false) List<Integer> danhGia,
						@RequestParam(required = false) List<String> loaiKhachSan,
						@RequestParam(required = false) List<Boolean> giapBien,
						@RequestParam(required = false) String thanhPho) {

		List<HotelDTO> danhSachKhachSan;

		if (thanhPho != null && !thanhPho.trim().isEmpty()) {
			danhSachKhachSan = hotelService.searchByCity(thanhPho);
		} else {
			danhSachKhachSan = hotelService.getAllAsDTO();
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
//    @GetMapping("/{id}")
//    public String chiTietKhachSan(@PathVariable Integer id, Model model) {
//        Optional<KhachSan> khachSan = khachSanService.findById(id);
//        if (khachSan.isPresent()) {
//            List<Phong> danhSachPhong = phongService.findByKhachSanId(id);
//            
//            model.addAttribute("khachSan", khachSan.get());
//            model.addAttribute("danhSachPhong", danhSachPhong);
//            model.addAttribute("danhSachBuaAn", BuaAnEnum.getAllBuaAn());
//            
//            return "chi-tiet-khach-san";
//        }
//        return "redirect:/khach-san";
//    }
//    
//    @GetMapping("/{id}/phong-trong")
//    public String kiemTraPhongTrong(@PathVariable Integer id,
//                                   @RequestParam("ngayDen") String ngayDenStr,
//                                   @RequestParam("ngayTra") String ngayTraStr,
//                                   Model model) {
//        try {
//            LocalDate ngayDen = LocalDate.parse(ngayDenStr);
//            LocalDate ngayTra = LocalDate.parse(ngayTraStr);
//            
//            Optional<KhachSan> khachSan = khachSanService.findById(id);
//            if (khachSan.isPresent()) {
//                List<Phong> phongTrong = phongService.findAvailableRooms(id, ngayDen, ngayTra);
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
}