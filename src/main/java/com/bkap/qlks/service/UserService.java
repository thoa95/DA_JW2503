package com.bkap.qlks.service;

import com.bkap.qlks.entity.Account;

public interface UserService {
	Account findByAccountId(String accountId);
	Boolean create(Account account);
}
