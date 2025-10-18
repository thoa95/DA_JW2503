package com.bkap.qlks.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {
	private Long id;
	private String name;
	private Integer area;
	private Integer price;
	private String description;
	private Integer bed;
	private String hotelName;
	private List<String> amenities;
}