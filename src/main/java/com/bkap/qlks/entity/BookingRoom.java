package com.bkap.qlks.entity;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bkap_booking_room")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookingRoom {
	@Id
	private Long bookingId;
	@Id
	private Long roomId;
	private String accountId;
	private Integer price;
	private Date checkInDate;
	private Date checkOutDate;
	private Date createdAt;
	private Date updateAt;
	private Integer deleteFlg;
	
}
