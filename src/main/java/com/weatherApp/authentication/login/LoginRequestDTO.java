package com.weatherApp.authentication.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequestDTO {

	private String username;
	private String password;
	
	public boolean isValid() {
		return username != null && password != null;
	}
}
