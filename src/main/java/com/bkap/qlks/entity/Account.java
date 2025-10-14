package com.bkap.qlks.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bkap_account")
@NoArgsConstructor
@AllArgsConstructor
public class Account {
	@Id
	private String accountId;
	private String password;
	private Integer gender;
	private String phone;
	private String email;
	private String role;
	
	
	
}
