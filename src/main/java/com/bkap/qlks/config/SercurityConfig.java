package com.bkap.qlks.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.bkap.qlks.service.CustomUserDetailService;


@Configuration
@EnableWebSecurity
public class SercurityConfig {

	@Autowired
	private CustomUserDetailService customUserDetailService;

	@Bean 
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()).authorizeHttpRequests((auth) -> auth.
				requestMatchers("/*").permitAll().
				requestMatchers("/details/**").permitAll().
				requestMatchers("/admin/**").hasAuthority("ADMIN").
				anyRequest().authenticated())
		.formLogin(login -> login.loginPage("/login").loginProcessingUrl("/login").
						usernameParameter("accountId").passwordParameter("password").
						defaultSuccessUrl("/admin",true)).logout(logout ->logout.logoutUrl("/logout").logoutSuccessUrl("/login"))
		.formLogin(login -> login.loginPage("/login").loginProcessingUrl("/login").
				usernameParameter("accountId").passwordParameter("password").
				defaultSuccessUrl("/",true)).logout(logout ->logout.logoutUrl("/logout").logoutSuccessUrl("/"));

		return http.build(); 
	}
	@Bean
	WebSecurityCustomizer webSecurityCustomizer () {
		 return (web) -> web.ignoring().requestMatchers(
		            "/static/**",
		            "/css/**",
		            "/js/**",
		            "/images/**",
		            "/fonts/**",
		            "/vendor/**",
		            "/bootstrap/**"
		        );
	}
}
