package com.weatherApp.authentication.signUp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

//Request DTO

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request {

	
	private String username;
	private String email;
	private String password;
	
	public boolean isValid() {
		
		return username != null && email !=	 null 
				&& password !=	null;
	}
	
}
