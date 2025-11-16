package com.bkap.qlks.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.bkap.qlks.entity.Room;
import com.bkap.qlks.repository.RoomRepository;

@Service
public class RoomService {
	@Autowired
	RoomRepository roomRepository;

	public Page<Room> getRoomsByHotelId(Long hotelID, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		return roomRepository.findByHotelId(hotelID, pageable);
	}

	public Page<Room> findAvailableRooms(Long hotelId, String checkIn, String checkOut, int numberPage, int size) {
		Pageable pageable = PageRequest.of(numberPage, size);
		return roomRepository.findAvailableRoomsNative(hotelId, checkIn, checkOut, pageable);
	}
	
	public Page<Room> searchQuick(Long hotelId, String checkIn, String checkOut,Integer bed, int numberPage, int size) {
		Pageable pageable = PageRequest.of(numberPage, size);
		return roomRepository.searchQuick(hotelId, checkIn, checkOut, bed, pageable);
	}


	public List<Room> getByInIds(List<Long> idRooms){
		return roomRepository.findRoomsByInIdList (idRooms);
	}
	
	public Room checkEmptyRoom(Long idRoom, String checkIn, String checkOut){
		return roomRepository.checkEmptyRoom (idRoom, checkIn, checkOut);
	}
	
	
}
