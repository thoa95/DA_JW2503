package com.bkap.qlks.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bkap.qlks.dto.HotelDTO;
import com.bkap.qlks.entity.Hotel;

@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {
	@Query("SELECT DISTINCT new com.bkap.qlks.dto.HotelDTO("
			+ "h.id, h.name, h.address, h.phone, h.description, h.nearSea, h.evaluate, "
			+ "c.name, c.urlImage, t.name) " + "FROM Hotel h " + "INNER JOIN City c ON h.cityId = c.id "
			+ "INNER JOIN TypeHotel t ON h.typeHotelId = t.id " + "WHERE :hotelId IS NULL OR h.id = :hotelId")
	List<HotelDTO> findAllAsDTO(@Param("hotelId") Long hotelId);

	@Query("SELECT new com.bkap.qlks.dto.HotelDTO("
			+ "h.id, h.name, h.address, h.phone, h.description, h.nearSea, h.evaluate, " 
			+ "c.name, c.urlImage,t.name) "
			+ "FROM Hotel h " 
			+ "LEFT JOIN City c ON h.cityId = c.id "
			+ "LEFT JOIN TypeHotel t ON h.typeHotelId = t.id "
			+ "WHERE (:cityId IS NULL OR c.id = :cityId)")
	List<HotelDTO> findAllByCity(@Param("cityId")Long cityId);

	@Query("""
			SELECT new com.bkap.qlks.dto.HotelDTO(
				h.id,
				h.name,
				h.address,
				h.phone,
				h.description,
				h.nearSea,
				h.evaluate,
			    c.name,
		        c.urlImage,
			    t.name)
			FROM Hotel h
			LEFT JOIN City c ON h.cityId = c.id
			LEFT JOIN TypeHotel t ON h.typeHotelId = t.id
			WHERE 
				 (:evaluateList IS NULL OR h.evaluate IN (:evaluateList))
			     AND (:typeHotelList IS NULL OR h.typeHotelId IN (:typeHotelList))
			     AND (:nearSea IS NULL OR h.nearSea = :nearSea)
			     AND (:cityList IS NULL OR h.cityId IN (:cityList))
			""")
	List<HotelDTO> searchHotel(
			@Param("evaluateList") List<Integer> evaluateList,
			@Param("typeHotelList") List<Long> typeHotelList,
			@Param("nearSea") Integer nearSea,
			@Param("cityList") List<Long> cityList);
	
	@Query("SELECT DISTINCT new com.bkap.qlks.dto.HotelDTO("
			+ "h.id,"
			+ " h.name,"
			+ " h.address,"
			+ " h.phone, h.description, h.nearSea, h.evaluate, "
			+ "c.name, c.urlImage, t.name) " 
			+ "FROM Room r " 
			+ "INNER JOIN Hotel h ON h.id = r.hotelId "
			+ "INNER JOIN City c ON h.cityId = c.id "
			+ "INNER JOIN TypeHotel t ON h.typeHotelId = t.id " 
			+ "WHERE r.id = :roomId")
	List<HotelDTO> findHotelByRoom(@Param("roomId") Long roomId);

}
