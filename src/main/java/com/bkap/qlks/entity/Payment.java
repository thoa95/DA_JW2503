package com.bkap.qlks.entity;

import java.util.Date;

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
@Table(name = "bkap_payment")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long bookingId;
	private String paymentMethod;
	private String vnpTxnRef;
	private Integer vnpAmount;
	private String vnpBankCode;
	private String vnpOrderInfo;
	private String vnpTransactionNo;
	private String vnpResponseCode;
	private String vnpTransactionStatus;
	private String paymentStatus;
	private Date createdAt;
	private Integer deleteFlg;
}
