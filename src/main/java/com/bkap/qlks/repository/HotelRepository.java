package com.bkap.qlks.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bkap.qlks.dto.HotelDTO;
import com.bkap.qlks.entity.Hotel;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
	@Query("SELECT new com.bkap.qlks.dto.HotelDTO(" +
            "h.id, h.name, h.address, h.phone, h.description, h.nearSea, h.evaluate, " +
            "c.name, t.name) " +
            "FROM Hotel h " +
            "INNER JOIN City c ON h.cityId = c.id " +
            "INNER JOIN TypeHotel t ON h.typeHotelId = t.id")
    List<HotelDTO> findAllAsDTO();

	@Query("SELECT new com.bkap.qlks.dto.HotelDTO(" +
            "h.id, h.name, h.address, h.phone, h.description, h.nearSea, h.evaluate, " +
            "c.name, t.name) " +
            "FROM Hotel h " +
            "LEFT JOIN City c ON h.cityId = c.id " +
            "LEFT JOIN TypeHotel t ON h.typeHotelId = t.id")
		List<HotelDTO> findAllByCity();
}
