package com.weatherApp.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.weatherApp.authentication.User;
import com.weatherApp.authentication.UserRepo;

import lombok.RequiredArgsConstructor;

@Service 
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService{

	private final UserRepo userRepo;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	
		User user = userRepo.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not with Username :" + username));
		
		return org.springframework.security.core.userdetails.User.builder()
				  .username(user.getUsername())
	                .password(user.getPassword()) // must be encoded!
	                .roles(user.getRole().toString()) // e.g., "USER" or "ADMIN"
	                .build();
	}
}
