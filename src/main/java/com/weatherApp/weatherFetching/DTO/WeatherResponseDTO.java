package com.weatherApp.weatherFetching.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WeatherResponseDTO {

	
	private String cityName;
	private String country;
	private Double temprature;
	private Double feelsLike;
	private Integer humidity;
	private Integer pressure;
	private String condition;
	private Double windSpeed;
	private Integer windDegree;
	private String windDirection;
	private Integer cloudiness;
	
	
	
}
