package com.bkap.qlks.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bkap.qlks.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
	Page<Room> findByHotelId(Long hotelID, Pageable pageable);
	
	@Query(value = "SELECT r FROM Room r where r.id IN (:roomIds)")
	List<Room> findRoomsByInIdList(List<Long> roomIds);

	@Query(value = """
			SELECT r.*
			FROM bkap_room r
			WHERE r.hotel_id = :hotelId
			  AND r.id NOT IN (
			    SELECT r2.id
			    FROM bkap_room r2
			    INNER JOIN bkap_hotel h ON r2.hotel_id = h.id
			    INNER JOIN bkap_booking_room br ON r2.id = br.room_id
			    INNER JOIN bkap_booking b ON br.booking_id = b.id
			    WHERE h.id = :hotelId
			      AND br.delete_flg = 0
			      AND (
			          (br.check_in_date >= TO_DATE(:checkIn, 'YYYY-MM-DD') AND br.check_out_date <= TO_DATE(:checkOut, 'YYYY-MM-DD'))
			          OR (TO_DATE(:checkIn, 'YYYY-MM-DD') BETWEEN br.check_in_date AND br.check_out_date)
			          OR (TO_DATE(:checkOut, 'YYYY-MM-DD') BETWEEN br.check_in_date AND br.check_out_date)
			      )
			      AND (
			          b.payment_status IN ('PENDING', 'PAID')
			          OR (
			              b.payment_status = 'UNPAID'
			              AND (SYSDATE - b.update_at) * 24 * 60 <= 20
			          )
			      )
			  )
			""", countQuery = """
			SELECT COUNT(*)
			FROM bkap_room r
			WHERE r.hotel_id = :hotelId
			  AND r.id NOT IN (
			    SELECT r2.id
			    FROM bkap_room r2
			    INNER JOIN bkap_hotel h ON r2.hotel_id = h.id
			    INNER JOIN bkap_booking_room br ON r2.id = br.room_id
			    INNER JOIN bkap_booking b ON br.booking_id = b.id
			    WHERE h.id = :hotelId
			      AND br.delete_flg = 0
			      AND (
			          (br.check_in_date >= TO_DATE(:checkIn, 'YYYY-MM-DD') AND br.check_out_date <= TO_DATE(:checkOut, 'YYYY-MM-DD'))
			          OR (TO_DATE(:checkIn, 'YYYY-MM-DD') BETWEEN br.check_in_date AND br.check_out_date)
			          OR (TO_DATE(:checkOut, 'YYYY-MM-DD') BETWEEN br.check_in_date AND br.check_out_date)
			      )
			      AND (
			          b.payment_status IN ('PENDING', 'PAID')
			          OR (
			              b.payment_status = 'UNPAID'
			              AND (SYSDATE - b.update_at) * 24 * 60 <= 20
			          )
			      )
			  )
			""", nativeQuery = true)
	Page<Room> findAvailableRoomsNative(@Param("hotelId") Long hotelId, @Param("checkIn") String checkIn,
			@Param("checkOut") String checkOut, Pageable pageable);
	
//	@Query(value = """
//			SELECT r.*
//			FROM bkap_room r
//			INNER JOIN bkap_hotel h ON r.hotel_id = h.id
//			INNER JOIN bkap_booking_room br ON r.id = br.room_id
//			INNER JOIN bkap_booking b ON br.booking_id = b.id
//			WHERE h.id = :hotelId
//			  AND (br.check_in_date >= TO_DATE(:checkIn, 'YYYY-MM-DD') AND br.check_out_date <= TO_DATE(:checkOut, 'YYYY-MM-DD'))
//			           OR (TO_DATE(:checkIn, 'YYYY-MM-DD') BETWEEN br.check_in_date AND br.check_out_date)
//			           OR (TO_DATE(:checkOut, 'YYYY-MM-DD') BETWEEN br.check_in_date AND br.check_out_date)
//			  AND br.delete_flg = 0
//			  AND (
//			      b.payment_status IN ('PENDING', 'PAID')
//			      OR (
//			          b.payment_status = 'UNPAID'
//			          AND (SYSDATE - b.update_at) * 24 * 60 <= 20
//			      )
//			  )
//			""", nativeQuery = true)
//	Page<Room> findBookedRooms(@Param("hotelId") Long hotelId, @Param("checkIn") String checkIn,
//			@Param("checkOut") String checkOut, Pageable pageable);

}
