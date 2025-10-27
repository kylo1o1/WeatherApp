package com.weatherApp.cityManagement.retrieve;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CityInfoDTO {

	
	private Long id;
	private String name;
	private String country;
	private String countryCode;
	private String timeZone;
	private Double longitude;
	private Double latitude;
	
}
