
package com.bkap.qlks.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.bkap.qlks.service.CustomLoginSuccessHandler;

@Configuration
@EnableWebSecurity
public class SercurityConfig {

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Autowired
    CustomLoginSuccessHandler loginSuccessHandler;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
		// ✅ Giữ nguyên session khi login, không mất pendingBooking
        .sessionManagement(session -> session
            .sessionFixation(sessionFixation -> sessionFixation.none())
        )
				.authorizeHttpRequests(
						(auth) -> auth.requestMatchers("/admin/**").hasAuthority("ADMIN")
						.requestMatchers("/**").permitAll()
								.requestMatchers("/details/**").permitAll()
								.anyRequest().authenticated())
				.formLogin(login -> login.loginPage("/login")
						.loginProcessingUrl("/login")
						.usernameParameter("accountId")
						.passwordParameter("password")
						 .successHandler(loginSuccessHandler) // ✅ dùng custom handler
			                .permitAll()
						)
			
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/"));

		return http.build();
	}

	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/static/**", "/css/**", "/js/**", "/images/**", "/fonts/**",
				"/vendor/**", "/bootstrap/**");
	}

}
