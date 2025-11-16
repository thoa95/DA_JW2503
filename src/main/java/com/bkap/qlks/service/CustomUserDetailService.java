package com.bkap.qlks.service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.bkap.qlks.entity.Account;
import com.bkap.qlks.models.CustomUserDetails;
import com.bkap.qlks.models.UserRole;
@Service
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	private UserService userService;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		Account account = userService.findByAccountId(username);
		if(account == null) {
			throw new UsernameNotFoundException("Sai");
		}
		Collection<GrantedAuthority> grantedAuthoritySet = new HashSet<>();
		
		Set<UserRole> roles = account.getUserRole();
		for (UserRole userRole : roles) {
			grantedAuthoritySet.add(new SimpleGrantedAuthority(userRole.getRoles().getName()));
		}
		return new CustomUserDetails(account, grantedAuthoritySet);
	}

}
