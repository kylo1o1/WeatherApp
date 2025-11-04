package com.weatherApp.common.exceptionHandling;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ErrorResponseBuilder {
	
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	public  ResponseEntity<Map<String, Object>> buildErrorResponse(
			String message,
			HttpStatus status
			){
		
		Map<String, Object> errorResponse = new HashMap<>();
		
		errorResponse.put("timeStamp", LocalDateTime.now().format(formatter).toString());
		
		errorResponse.put("status", status.value());
		errorResponse.put("error", status.getReasonPhrase());
		errorResponse.put("message", message);
		
		return new ResponseEntity<>(errorResponse,status);
		
		
	}
	
	
}
