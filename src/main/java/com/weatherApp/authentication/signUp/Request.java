package com.weatherApp.authentication.signUp;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Request DTO

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request {

	
	@NotBlank(message = "username is required")
	private String username;
	
	@NotBlank(message = "Email is required")
	@Email(message = "Invalid email format")
	private String email;
	
	
	@NotBlank(message = "Password is required")
	@Pattern(
			
			regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{5,}$",
	        message = "Password must be at least 5 characters, contain an uppercase letter, number, and special character"			)
	private String password;
	
	
	
	
}
