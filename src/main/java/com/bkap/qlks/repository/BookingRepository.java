package com.bkap.qlks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bkap.qlks.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>{

}
