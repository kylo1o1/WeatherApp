package com.weatherApp.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	

	private  final JwtAuthenticationFilter jwtAuthenticationFilter;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http
			.csrf(csrf -> csrf.disable())
			.authorizeHttpRequests(auth -> 
			
					auth
					.requestMatchers("/", "/login", "/signup", "/css/**", "/js/**", "/images/**").permitAll()
					.requestMatchers("/api/auth/**").permitAll()
		            .requestMatchers("/dashboard", "/admin/**").authenticated()
					.anyRequest().authenticated()
					)
					.sessionManagement(session -> session
					
					.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
					
					)
			
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			
			;
		return http.build();
			
		
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		
		return new BCryptPasswordEncoder();
	}
	
	
	
}
