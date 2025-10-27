package com.weatherApp.weatherFetching;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherApp.common.exceptionHandling.CustomExceptions.WeatherApiException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WeatherClient {

	private final RestTemplate restTemplate;
	private final ObjectMapper objectMapper;
	
	@Value("${api.secret.key}")
	private String apiKey;
	
	@Value("${api.base.url}")
	private String BASE_URL;

	
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

	        // Location info
	        weatherData.setCityName(location.get("name").asText());
	        weatherData.setCountry(location.get("country").asText());

	        // Current weather info
	        weatherData.setTemprature(current.get("temp_c").asDouble());
	        weatherData.setFeelsLike(current.get("feelslike_c").asDouble());
	        weatherData.setHumidity(current.get("humidity").asInt());
	        weatherData.setPressure(current.get("pressure_mb").asInt());

	        // Condition
	        JsonNode condition = current.get("condition");
	        weatherData.setCondition(condition.get("text").asText());

	        // Wind details
	        weatherData.setWindDegree(current.get("wind_degree").asInt());
	        weatherData.setWindSpeed(current.get("wind_kph").asDouble());
	        weatherData.setWindDirection(current.get("wind_dir").asText());

	        // Clouds
	        weatherData.setCloudiness(current.get("cloud").asInt());

	        log.info("Weather Fetched Successfully : {}", weatherData);

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
