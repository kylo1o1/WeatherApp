package com.weatherApp.api;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherApp.common.exceptionHandling.CustomExceptions.WeatherApiException;
import com.weatherApp.weatherFetching.WeatherData;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApiClient {

	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;
	
	@Value("${api.secret.key}")
	private String apiKey;
	
	@Value("${api.base.url}")
	private String BASE_URL;
	
	
	
	public boolean isCityValid(String cityName,String country) {
		
		String url = String.format(
				"%s?key=%s&q=%s&aqi=no",
				BASE_URL,
				apiKey,
				cityName
				);
		
		try {
			String jsonResponse = restTemplate.getForObject(url, String.class);

			JsonNode root = objectMapper.readTree(jsonResponse);
			
			
			JsonNode location = root.get("location");	        
			String apiCityName = location.get("name").asText();
			

	        return apiCityName.equalsIgnoreCase(cityName.trim());

			
			
		} catch (HttpClientErrorException e) {
			
			return false;
		} catch (Exception e) {
			// TODO Auto-generated catch block
	        throw new RuntimeException("Error validating city", e);		
	        }
	
	}
	
	public WeatherData fetchWeather(String cityName) {
		
		
	 String url = String.format(
				"%s?key=%s&q=%s&aqi=no",
				BASE_URL,
				apiKey,
				cityName
				);
		
		try {
			
			String jsonResponse = restTemplate.getForObject(url, String.class);
			
			JsonNode root = objectMapper.readTree(jsonResponse);
			
			WeatherData weatherData = new WeatherData();
			JsonNode location = root.get("location");
	        JsonNode current = root.get("current");

	        weatherData.setCityName(location.get("name").asText());
	        weatherData.setCountry(location.get("country").asText());

	        weatherData.setTemprature(current.get("temp_c").asDouble());
	        weatherData.setFeelsLike(current.get("feelslike_c").asDouble());
	        weatherData.setHumidity(current.get("humidity").asInt());
	        weatherData.setPressure(current.get("pressure_mb").asInt());

	        JsonNode condition = current.get("condition");
	        weatherData.setCondition(condition.get("text").asText());

	        weatherData.setWindDegree(current.get("wind_degree").asInt());
	        weatherData.setWindSpeed(current.get("wind_kph").asDouble());
	        weatherData.setWindDirection(current.get("wind_dir").asText());

	        weatherData.setCloudiness(current.get("cloud").asInt());


	        return weatherData;
			
			
		} catch (Exception e) {
			
			log.error("Error Fetching from WeatherAPI.com : {}" ,e.getMessage());
			
			if(e.getMessage().contains("1006")) {
				
				throw new WeatherApiException(
						"City " + cityName + " is not in weather Service "
						);
			}else if(e.getMessage().contains("2008")) {
				
				throw new WeatherApiException(
						"Weather API key is invalid or disabled"
						);
				
			}else if(e.getMessage().contains("2009")) {
				throw new WeatherApiException(
						
						"Weather API key is disabled or  exceeded quota"
						);
			}
			
			throw new WeatherApiException(
					"Failed to fetch weather data : " + e.getMessage()
					);
		}
		
		
	}
	
	
}
