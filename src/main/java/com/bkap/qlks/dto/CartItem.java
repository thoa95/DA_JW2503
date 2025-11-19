package com.bkap.qlks.dto;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
	private Long roomId;
    private String roomName;
    private Integer price;
    private String fromDate;
    private String toDate;
    private Integer numberDay;
    private Integer totalPrice;
    
    public CartItem(Long roomId, String roomName, Integer price, String fromDate, String toDate) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.price = price;
        this.fromDate = fromDate;
        this.toDate = toDate;
        this.numberDay = calculateNumberDay(fromDate, toDate);
    }

    public static int calculateNumberDay(String from, String to) {
        if (from == null || to == null) return 1;
        LocalDate start = LocalDate.parse(from);
        LocalDate end = LocalDate.parse(to);
        int days = (int) ChronoUnit.DAYS.between(start, end) +1; 
        return days <= 0 ? 1 : days;
    }
}
