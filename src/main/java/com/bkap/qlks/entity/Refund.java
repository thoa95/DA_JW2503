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
@Table(name = "bkap_refund")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Refund {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long paymentId;
	private Integer refundAmount;
	private String refundReason;
	private String refundStatus;
	private Date refundDate;
	private Integer deleteFlg;
}
