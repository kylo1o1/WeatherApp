package com.weatherApp.authentication.login;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {

	@NotBlank(message = "username is required")
	private String username;
	
	@NotBlank(message = "password is required")
	private String password;
	
}
