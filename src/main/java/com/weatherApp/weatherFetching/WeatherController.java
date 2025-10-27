package com.weatherApp.weatherFetching;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.weatherApp.common.exceptionHandling.CustomExceptions.WeatherApiException;
import com.weatherApp.weatherFetching.DTO.WeatherRequestDTO;
import com.weatherApp.weatherFetching.DTO.WeatherResponseDTO;

import lombok.RequiredArgsConstructor;

@RequestMapping("/api/weather")
@RestController
@RequiredArgsConstructor
public class WeatherController {

	private final FetchWeatherForCity fetchWeatherForCity;
	
	
	@GetMapping("/{cityName}")
	public ResponseEntity<?> getWeather(@PathVariable String cityName) throws Exception{
	
			WeatherRequestDTO request = new WeatherRequestDTO(cityName);
			
			WeatherResponseDTO response = fetchWeatherForCity.execute(request);
			
			return ResponseEntity.ok(response);
		
	}
	
}
