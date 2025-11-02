package com.bkap.qlks.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class PaymentDTO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String status;
	private String message;
	private String URL;

}
