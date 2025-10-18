package com.bkap.qlks.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bkap_hotel")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Hotel {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private String address;
	private String phone;
	private String description;
	private Integer nearSea;
	private Integer evaluate;
	private Long cityId;
	private Long typeHotelId;
	private String staffId;
}
