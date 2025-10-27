package com.weatherApp.common.exceptionHandling;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

import com.weatherApp.common.exceptionHandling.CustomExceptions.CityNotFoundException;
import com.weatherApp.common.exceptionHandling.CustomExceptions.DuplicateCityException;
import com.weatherApp.common.exceptionHandling.CustomExceptions.DuplicateUsernameException;
import com.weatherApp.common.exceptionHandling.CustomExceptions.MissingDataException;
import com.weatherApp.common.exceptionHandling.CustomExceptions.WeatherApiException;

import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j

public class GlobalExceptionHandler {
	
	
	private ResponseEntity<Map<String, Object>> buildErrorResponse(
			String message,
			HttpStatus status
			){
		
		Map<String, Object> errorResponse = new HashMap<>();
		
		errorResponse.put("timeStamp", LocalDateTime.now().toString());
		
		errorResponse.put("status", status.value());
		errorResponse.put("error", status.getReasonPhrase());
		errorResponse.put("message", message);
		
		return new ResponseEntity<>(errorResponse,status);
		
		
	}
	
	@ExceptionHandler(HttpClientErrorException.class)
	public ResponseEntity<?> handleExternalApiError(HttpClientErrorException ex){
		
		String message;
		
		if(ex.getStatusCode().value() == 401) {
			
			message = "Weather API key is invalid or expired";
			
		}else if(ex.getStatusCode().value() == 404) {
			message = "City not found in weather service";
		}else {
			 message = "Weather service error: " + ex.getStatusText();
		}
		
		return buildErrorResponse(message, HttpStatus.BAD_GATEWAY);
	}
	@ExceptionHandler(WeatherApiException.class)
	public ResponseEntity<?> handleWeatherApiError(WeatherApiException ex){
		
		return buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		
		
	}
	
	@ExceptionHandler(CityNotFoundException.class)
	public ResponseEntity<?> handleCityNotFound(CityNotFoundException ex){
		return buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
		
	}
	
	@ExceptionHandler(MissingDataException.class)
	public ResponseEntity<?> handleMissingData(MissingDataException ex){
		
		return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
		
	}
	
	@ExceptionHandler({DuplicateUsernameException.class,DuplicateCityException.class})
	public ResponseEntity<?> handleDuplicateResource(RuntimeException ex){
		
		return buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
	}
	
	
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<?> handleAccessDenials(AccessDeniedException ex){
		
		return buildErrorResponse(ex.getMessage(), HttpStatus.FORBIDDEN);
		
		
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<?> handleBadCredentials(BadCredentialsException ex){
		
		return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
		
		
	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGenericException (Exception ex){
		
		return buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
