package com.bkap.qlks.models;

import com.bkap.qlks.entity.Account;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_role")
public class UserRole {

	@Id
	@Column(name = "id")

	private String id;
	
	@ManyToOne
	@JoinColumn(name = "userId", referencedColumnName = "accountId")
	private Account account;
	@ManyToOne
	@JoinColumn(name = "roleId", referencedColumnName = "id")
	private Roles roles;
	
	public UserRole() {
		// TODO Auto-generated constructor stub
	}

	public UserRole(String id, Account account, Roles roles) {
		super();
		this.id = id;
		this.account = account;
		this.roles = roles;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Roles getRoles() {
		return roles;
	}

	public void setRoles(Roles roles) {
		this.roles = roles;
	}
	
	
	
}
