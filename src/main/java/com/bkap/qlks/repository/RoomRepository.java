package com.bkap.qlks.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bkap.qlks.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long>{
	List<Room> findByHotelId(Long hotelID);
	
//	@Query("SELECT DISTINCT new com.bkap.qlks.dto.RoomDTO(" +
//		    "h.id, h.name, h.address, h.phone, h.description, h.nearSea, h.evaluate, " +
//		    "c.name, c.urlImage, t.name) " +
//		    "FROM Room r " +
//		    "LEFT JOIN Amenity am ON r.id = am.id " +
//		    "INNER JOIN TypeHotel t ON h.typeHotelId = t.id " +
//		    "WHERE :hotelId IS NULL OR h.id = :hotelId"
}
