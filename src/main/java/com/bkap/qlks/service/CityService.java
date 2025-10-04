package com.bkap.qlks.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bkap.qlks.entity.City;
import com.bkap.qlks.repository.CityRepository;

@Service
public class CityService {
	@Autowired
	CityRepository cityRepository;
	
	public List<City> getAll() {
		return cityRepository.findAll();
	}
	
}
