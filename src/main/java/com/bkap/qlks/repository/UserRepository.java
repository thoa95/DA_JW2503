package com.bkap.qlks.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bkap.qlks.entity.Account;


public interface UserRepository extends JpaRepository<Account, String> {

	Account findByAccountId(String accountId);
}
