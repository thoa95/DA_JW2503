package com.bkap.qlks.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bkap.qlks.entity.Room;
import com.bkap.qlks.repository.RoomRepository;

@Service
public class RoomService {
	@Autowired
	RoomRepository roomRepository;

	public List<Room> getRoomsByHotelId(Long hotelID) {
		return roomRepository.findByHotelId(hotelID);
	}
	
	
}
