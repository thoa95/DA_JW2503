package com.bkap.qlks.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bkap.qlks.dto.HotelDTO;
import com.bkap.qlks.entity.Hotel;
import com.bkap.qlks.repository.HotelRepository;
import com.bkap.qlks.util.VNCharacterUtils;

@Service
public class HotelService {

	@Autowired
	HotelRepository hotelRepository;

	public List<Hotel> getAll() {
		return hotelRepository.findAll();
	}

	public List<HotelDTO> getAllAsDTO(Long id) {
		return hotelRepository.findAllAsDTO(id);
	}

	public List<HotelDTO> searchByCity(String tenThanhPho) {
		String tenKhongDau = VNCharacterUtils.removeAccent(tenThanhPho.toLowerCase());
		return hotelRepository.findAllByCity().stream()
				.filter(ks -> VNCharacterUtils.removeAccent(ks.getCityName().toLowerCase()).contains(tenKhongDau))
				.collect(Collectors.toList());
	}

	public List<HotelDTO> filterKhachSan(
										List<HotelDTO> khachSanList,
										List<Integer> danhGiaFilter,
										List<String> loaiKhachSanFilter,
										List<Boolean> giapBienFilter) {
		return khachSanList.stream().filter(ks -> filterByDanhGia(ks, danhGiaFilter))
				.filter(ks -> filterByLoaiKhachSan(ks, loaiKhachSanFilter))
//				.filter(ks -> filterByGiapBien(ks, giapBienFilter))
				.collect(Collectors.toList());
	}
	
	
    private boolean filterByDanhGia(HotelDTO ks, List<Integer> filter) {
        return filter == null || filter.isEmpty() || filter.contains(ks.getEvaluate());
    }
    
    private boolean filterByLoaiKhachSan(HotelDTO ks, List<String> filter) {
        return filter == null || filter.isEmpty() || filter.contains(ks.getTypeHotelName());
    }
    
    
//    private boolean filterByGiapBien(HotelDTO ks, List<Boolean> filter) {
//        return filter == null || filter.isEmpty() || filter.contains(ks.getNearSea());
//    }
    
    
    public Optional<Hotel> getByHotelId(Long id) {
    	return hotelRepository.findById(id);
    }
    
    
    
}
