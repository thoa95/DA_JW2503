
package com.bkap.qlks.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.bkap.qlks.service.CustomLoginSuccessHandler;
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
	
	@Autowired
    private CustomLoginSuccessHandler loginSuccessHandler;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
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
				.logout(logout -> logout.logoutUrl("/logout").logoutSuccessUrl("/login"))
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

	// ✅ Bổ sung: AuthenticationProvider bỏ qua kiểm tra password
	@Bean
	public AuthenticationProvider customAuthenticationProvider() {
		return new AuthenticationProvider() {
			@Override
			public Authentication authenticate(Authentication authentication) throws AuthenticationException {
				String username = authentication.getName();

				// Gọi lại service của bạn
				var userDetails = customUserDetailService.loadUserByUsername(username);

				// ❌ Bỏ qua bước so sánh password, chỉ cần user tồn tại
				return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
			}

			@Override
			public boolean supports(Class<?> authentication) {
				return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
			}
		};
	}
}