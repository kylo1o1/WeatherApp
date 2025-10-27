package com.weatherApp.cityManagement.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor

@NoArgsConstructor
public class CreateCityResponseDTO {

	private Long id;
	private String cityName;
	private String country;
	private String countryCode;
	private String timeZone;
	private Double longitude;
	private Double latitude;
	
	private String message;

	
}
