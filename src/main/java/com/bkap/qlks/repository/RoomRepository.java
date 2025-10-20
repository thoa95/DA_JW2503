package com.bkap.qlks.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.bkap.qlks.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
	List<Room> findByHotelId(Long hotelID);

	@Query(value = """
			SELECT r.*
			FROM bkap_room r
			WHERE r.id NOT IN (
			    SELECT r.id
			    FROM bkap_room r
			    INNER JOIN bkap_hotel h ON r.hotel_id = h.id
			    INNER JOIN bkap_booking_room br ON r.id = br.room_id
			    INNER JOIN bkap_booking b ON br.booking_id = b.id
			    WHERE h.id = :hotelId
			      AND br.check_in_date >= :checkIn
			      AND br.check_out_date <= :checkOut
			      AND br.delete_flg = 0
			      AND (
			          b.payment_status IN ('PENDING', 'PAID')
			          OR (
			              b.payment_status = 'UNPAID'
			              AND (SYSDATE - b.updated_at) * 24 * 60 <= 20
			          )
			      )
			)
			""", nativeQuery = true)
			List<Room> findAvailableRoomsNative(
			    @Param("hotelId") Long hotelId,
			    @Param("checkIn") Date checkIn,
			    @Param("checkOut") Date checkOut
			);

	@Query(value = """
			SELECT r.*
			FROM bkap_room r
			INNER JOIN bkap_hotel h ON r.hotel_id = h.id
			INNER JOIN bkap_booking_room br ON r.id = br.room_id
			INNER JOIN bkap_booking b ON br.booking_id = b.id
			WHERE h.id = :hotelId
			  AND br.check_in_date >= :checkIn
			  AND br.check_out_date <= :checkOut
			  AND br.delete_flg = 0
			  AND (
			      b.payment_status IN ('PENDING', 'PAID')
			      OR (
			          b.payment_status = 'UNPAID'
			          AND (SYSDATE - b.updated_at) * 24 * 60 <= 20
			      )
			  )
			""", nativeQuery = true)
			List<Room> findBookedRooms(
			    @Param("hotelId") Long hotelId,
			    @Param("checkIn") Date checkIn,
			    @Param("checkOut") Date checkOut
			);


}
