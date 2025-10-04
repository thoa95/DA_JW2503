package com.bkap.qlks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bkap.qlks.entity.TypeHotel;

@Repository
public interface TypeHotelRepository extends JpaRepository<TypeHotel, Long>{

}
