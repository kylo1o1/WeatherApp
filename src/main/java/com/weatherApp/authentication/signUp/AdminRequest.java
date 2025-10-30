package com.weatherApp.authentication.signUp;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminRequest {

	@Valid
	@NotNull
	private  Request user;
	
	@NotBlank(message = "Secrety key is required")
	private  String secretKey;
	
}
