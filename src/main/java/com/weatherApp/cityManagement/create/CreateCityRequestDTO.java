package com.weatherApp.cityManagement.create;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CreateCityRequestDTO {

	
	@NotBlank(message = "City name is required")
	private String name;

	@NotBlank(message = "Country is required")
	private String country;
	
	@NotBlank(message = "Country code is required")
	private String countryCode;
	
	
}
