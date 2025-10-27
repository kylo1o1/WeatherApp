package com.weatherApp.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class ApplicationConfig {

	
	
	@Bean
	RestTemplate restTemplate() {
		
		RestTemplate restTemplate = new RestTemplate();
		SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
		factory.setConnectTimeout(5000);
		factory.setReadTimeout(5000);
		restTemplate.setRequestFactory(factory);
		
		return restTemplate;
	}
	
	@Bean 
	ObjectMapper objectMapper() {
		
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper;
	}
}
