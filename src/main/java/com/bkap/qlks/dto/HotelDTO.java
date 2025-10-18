package com.bkap.qlks.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelDTO {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String description;
    private Integer nearSea;
    private Integer evaluate;
    private String cityName;
    private String urlCityImage;
    private String typeHotelName;
}