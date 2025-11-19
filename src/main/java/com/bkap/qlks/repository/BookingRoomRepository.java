package com.bkap.qlks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bkap.qlks.entity.BookingRoom;
import com.bkap.qlks.entity.BookingRoomId;

@Repository
public interface BookingRoomRepository extends JpaRepository<BookingRoom, BookingRoomId>{

}
