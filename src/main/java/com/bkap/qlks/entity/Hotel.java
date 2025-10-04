package com.bkap.qlks.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bkap_hotel")
@NoArgsConstructor
@AllArgsConstructor
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
	public Long getCityId() {
		return cityId;
	}
	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}
	public Long getTypeHotelId() {
		return typeHotelId;
	}
	public void setTypeHotelId(Long typeHotelId) {
		this.typeHotelId = typeHotelId;
	}
	private Long cityId;
	private Long typeHotelId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getNearSea() {
		return nearSea;
	}
	public void setNearSea(Integer nearSea) {
		this.nearSea = nearSea;
	}
	public Integer getEvaluate() {
		return evaluate;
	}
	public void setEvaluate(Integer evaluate) {
		this.evaluate = evaluate;
	}
	
	
	
}
