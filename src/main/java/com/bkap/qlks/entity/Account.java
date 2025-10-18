package com.bkap.qlks.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bkap_account")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Account {
	@Id
	private String accountId;
	private String password;
	private Integer gender;
	private String phone;
	private String email;
	private String role;	
}
