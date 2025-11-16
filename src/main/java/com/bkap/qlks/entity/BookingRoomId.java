package com.bkap.qlks.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

@SuppressWarnings("serial")
@Embeddable
@Data
public class BookingRoomId implements Serializable {
    @Column(name = "booking_id")
    private Long bookingId;

	@Column(name = "room_id")
    private Long roomId;
}
