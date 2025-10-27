package com.weatherApp.weatherFetching;

import com.weatherApp.cityManagement.City;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;



@Data

@AllArgsConstructor
@NoArgsConstructor
public class WeatherData {

	
	
	private String cityName;
	private String country;
	
	//Temp
	
	private Double temprature;
	private Double feelsLike;

//	Atmosphere 
	
	private Integer humidity;
	private Integer pressure;
	
	
//	Weather Condition
	
	private String condition;
	
//	Wind 
	
	private Double windSpeed;
	private Integer windDegree;
	private String windDirection;
	
	private Integer cloudiness;
	

	
	
}
