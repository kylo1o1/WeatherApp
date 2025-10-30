package com.weatherApp.authentication;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weatherApp.authentication.login.Login;
import com.weatherApp.authentication.login.LoginRequestDTO;
import com.weatherApp.authentication.login.LoginResponseDTO;
import com.weatherApp.authentication.signUp.AdminRequest;
import com.weatherApp.authentication.signUp.Request;
import com.weatherApp.authentication.signUp.Response;
import com.weatherApp.authentication.signUp.SignUp;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

	@Autowired
	private  SignUp signUpUseCase;
	
	@Autowired
	private Login loginUseCase;
	
	
	
	@PostMapping("/login")
	public  ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO loginRequestDTO){
		
			
			LoginResponseDTO response = loginUseCase.execute(loginRequestDTO);
			
			return  ResponseEntity.ok(response);
			
	}
	
	@PostMapping("/signUp")
	public ResponseEntity<?> signUp(@Valid @RequestBody Request request){
		
			Response response = signUpUseCase.execute(request);
			
			return ResponseEntity.ok(response);
	
		
	}
	
	@PostMapping("/signUp/admin")
	public ResponseEntity<?> signUpForAdmin(@Valid @RequestBody AdminRequest request){
	
			Response response = signUpUseCase.executeAsAdmin(request);
			
			return ResponseEntity.ok(response);
			
		
	}
	
	
 }
