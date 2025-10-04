package com.bkap.qlks.dto;

public class HotelDTO {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String description;
    private Integer nearSea;
    private Integer evaluate;

    private String cityName;
    private String typeHotelName;
    
    
    
	public HotelDTO() {
		super();
	}
	public HotelDTO(Long id, String name, String address, String phone, String description, Integer nearSea,
			Integer evaluate, String cityName, String typeHotelName) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.phone = phone;
		this.description = description;
		this.nearSea = nearSea;
		this.evaluate = evaluate;
		this.cityName = cityName;
		this.typeHotelName = typeHotelName;
	}
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
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public String getTypeHotelName() {
		return typeHotelName;
	}
	public void setTypeHotelName(String typeHotelName) {
		this.typeHotelName = typeHotelName;
	}
    
    
}