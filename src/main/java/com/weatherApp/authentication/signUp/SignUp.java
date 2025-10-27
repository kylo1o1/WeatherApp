package com.weatherApp.authentication.signUp;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.weatherApp.authentication.User;
import com.weatherApp.authentication.UserRepo;
import com.weatherApp.common.exceptionHandling.CustomExceptions.DuplicateUsernameException;
import com.weatherApp.common.exceptionHandling.CustomExceptions.MissingDataException;
import com.weatherApp.security.Role;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignUp {

	
	private final UserRepo userRepo;
	
	private final  PasswordEncoder passwordEncoder;
	
	
	public Response execute(Request request) {
		
		if(!request.isValid()) {
			throw new MissingDataException("User Details are incomplete");
		}
		
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
	
	public Response executeAsAdmin(Request request) {
		
		if(!request.isValid()) {
			throw new MissingDataException("User Details are incomplete");
		}
		
		if(userRepo.findByUsername(request.getUsername()).isPresent()) {
			
			throw new DuplicateUsernameException("Username Already Exists");
		}
		
		
		User newUser = new User();
		
		newUser.setUsername(request.getUsername());
		newUser.setEmail(request.getEmail());
		
		
		String encryptedPassword = passwordEncoder.encode(request.getPassword());
		
		newUser.setPassword(encryptedPassword);
		newUser.setRole(Role.ADMIN);
		
		userRepo.save(newUser);
		
		return new Response(
				"Admin User Registered Successfully",
				newUser.getUsername()
				);
		
	}
}
