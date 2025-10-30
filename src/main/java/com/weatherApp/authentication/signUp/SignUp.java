package com.weatherApp.authentication.signUp;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.weatherApp.authentication.User;
import com.weatherApp.authentication.UserRepo;
import com.weatherApp.common.exceptionHandling.CustomExceptions.DuplicateUsernameException;
import com.weatherApp.security.Role;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignUp {

	
	private final UserRepo userRepo;
	
	private final  PasswordEncoder passwordEncoder;
	
	@Value("${admin.secret.key}")
	private String ADMIN_SECRET_KEY ;
	
	public Response execute(Request request) {
		
		
		if(userRepo.findByUsername(request.getUsername()).isPresent()) {
			
			throw new DuplicateUsernameException("Username Already Exists");
		}
		
		
		User newUser = new User();
		
		newUser.setUsername(request.getUsername());
		newUser.setEmail(request.getEmail());
		
		
		String encryptedPassword = passwordEncoder.encode(request.getPassword());
		
		newUser.setPassword(encryptedPassword);
		newUser.setRole(Role.USER);
		
		userRepo.save(newUser);
		
		return new Response(
				"User Registered Successfully",
				newUser.getUsername()
				);
		
	}
	
	public Response executeAsAdmin(AdminRequest request) {
		
		
		Request user = request.getUser();
		
		if(!ADMIN_SECRET_KEY.equals(request.getSecretKey().trim())) {
			throw new BadCredentialsException("Invalid Secret Key");
		}
		
		if(userRepo.findByUsername(user.getUsername()).isPresent()) {
			
			throw new DuplicateUsernameException("Username Already Exists");
		}
		
		
		User admin = new User();
		
		admin.setUsername(user.getUsername());
		admin.setEmail(user.getEmail());
		
		
		String encryptedPassword = passwordEncoder.encode(user.getPassword());
		
		admin.setPassword(encryptedPassword);
		admin.setRole(Role.ADMIN);
		
		userRepo.save(admin);
		
		return new Response(
				"Admin User Registered Successfully",
				admin.getUsername()
				);
		
	}
}
