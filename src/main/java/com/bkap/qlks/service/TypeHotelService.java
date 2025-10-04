package com.bkap.qlks.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bkap.qlks.entity.TypeHotel;
import com.bkap.qlks.repository.TypeHotelRepository;

@Service
public class TypeHotelService {
	
	@Autowired
	TypeHotelRepository typeHotelRepository;
	
	public List<TypeHotel> getAll() {
		return typeHotelRepository.findAll();
	}
	
}
