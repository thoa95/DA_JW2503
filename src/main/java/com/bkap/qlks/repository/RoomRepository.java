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

	@Query("""
			SELECT r FROM Room r
			WHERE r.id NOT IN (
				SELECT r
				   FROM Room r
				   INNER JOIN Hotel h ON r.hotelId = h.id
				   INNER JOIN BookingRoom br ON r.id = br.room.id
				   INNER JOIN Booking b ON br.booking.id = b.id
				   WHERE h.id = :hotelId
				     AND br.checkInDate >= :checkIn
				     AND br.checkOutDate <= :checkOut
				     AND br.deleteFlg = '0'
				     AND (
				           b.paymentStatus IN ('PENDING', 'PAID')
				           OR (
				               b.paymentStatus = 'UNPAID'
				               AND (CURRENT_TIMESTAMP - b.updatedAt) * 24 * 60 <= 20
				           )
				         )
				      )
				""")
	List<Room> findAvailableRooms(
			@Param("hotelId") Long hotelId,
			@Param("checkIn") Date checkIn,
			@Param("checkOut") Date checkOut);

	@Query("""
			SELECT r
			FROM Room r
			INNER JOIN Hotel h ON r.hotelId = h.id
			INNER JOIN BookingRoom br ON r.id = br.room.id
			INNER JOIN Booking b ON br.booking.id = b.id
			WHERE h.id = :hotelId
			  AND br.checkInDate >= :checkIn
			  AND br.checkOutDate <= :checkOut
			  AND br.deleteFlg = '0'
			  AND (
			        b.paymentStatus IN ('PENDING', 'PAID')
			        OR (
			            b.paymentStatus = 'UNPAID'
			            AND (CURRENT_TIMESTAMP - b.updatedAt) * 24 * 60 <= 20
			        )
			      )
			""")
	List<Room> findRoomsBooked(
			@Param("hotelId") Long hotelId,
			@Param("checkIn") Date checkIn,
			@Param("checkOut") Date checkOut);

}
