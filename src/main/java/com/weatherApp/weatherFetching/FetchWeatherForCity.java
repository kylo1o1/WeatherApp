package com.weatherApp.weatherFetching;

import org.springframework.stereotype.Service;

import com.weatherApp.cityManagement.CityRepo;
import com.weatherApp.common.exceptionHandling.CustomExceptions.CityNotFoundException;
import com.weatherApp.weatherFetching.DTO.WeatherRequestDTO;
import com.weatherApp.weatherFetching.DTO.WeatherResponseDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FetchWeatherForCity {

	private final CityRepo cityRepo;
	private final WeatherClient weatherClient;
	
	
	public WeatherResponseDTO execute(WeatherRequestDTO request) throws Exception {
		
		
		if(request.getCity() == null || request.getCity().trim().isEmpty()) {
			throw new IllegalArgumentException("City name is required");
		}
		
		String cityName = request.getCity();
		
		if(cityRepo.findByName(cityName).isEmpty()) {
			throw new CityNotFoundException(
					
					"City '" + cityName + "' is not in approved list"  
					);
		}
		
		WeatherData weatherData = weatherClient.fetchWeather(cityName);
		
		return mapData(weatherData);
		
	}
	
	private WeatherResponseDTO mapData(WeatherData weatherData) {
		
		WeatherResponseDTO response = new WeatherResponseDTO();
		
		response.setCityName(weatherData.getCityName());
		response.setCountry(weatherData.getCountry());
		response.setTemprature(weatherData.getTemprature());
		response.setHumidity(weatherData.getHumidity());
		response.setFeelsLike(weatherData.getFeelsLike());
		response.setPressure(weatherData.getPressure());
		response.setCondition(weatherData.getCondition());
		response.setWindDegree(weatherData.getWindDegree());
		response.setWindDirection(weatherData.getWindDirection());
		response.setWindSpeed(weatherData.getWindSpeed());
		response.setCloudiness(weatherData.getCloudiness());
		
		return response;
		
		
	}
	
}
