package com.weatherApp.authentication.login;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.weatherApp.authentication.User;
import com.weatherApp.authentication.UserRepo;
import com.weatherApp.common.exceptionHandling.CustomExceptions.MissingDataException;
import com.weatherApp.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class Login {

	private final UserRepo userRepo;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;
	
	public LoginResponseDTO execute(LoginRequestDTO requestDTO) {
		if(!requestDTO.isValid()) {
			throw new MissingDataException("Incomplete Credentials");
		}
		
		User user = userRepo.findByUsername(requestDTO.getUsername())
				.orElseThrow(() -> new BadCredentialsException("Invalid Username or password"));
	

		boolean passwordMatch = passwordEncoder.matches(requestDTO.getPassword(), user.getPassword());
		if(!passwordMatch) {
			throw new BadCredentialsException("Invalid username or password");
		}
		
		String token = jwtTokenProvider.generateToken(user.getUsername(), user.getRole().name());
		
		
		return new LoginResponseDTO(
				token,
				user.getUsername(),
				user.getRole().name()
				);
	}
	
}
