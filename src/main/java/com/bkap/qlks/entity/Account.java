package com.bkap.qlks.entity;

import java.util.Set;

import com.bkap.qlks.models.UserRole;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
	private String full_name;
	private Integer gender;
	private String phone;
	private String email;
	private String role;	
	@OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
	private Set<UserRole> userRole;
	@OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
	private Set<News> news;
	
	
}
