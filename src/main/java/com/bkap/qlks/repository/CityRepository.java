package com.bkap.qlks.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bkap.qlks.entity.City;

@Repository
public interface CityRepository extends JpaRepository<City, Long>{

}
