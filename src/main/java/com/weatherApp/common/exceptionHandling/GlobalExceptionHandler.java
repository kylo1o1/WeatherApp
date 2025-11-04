package com.weatherApp.common.exceptionHandling;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.weatherApp.common.exceptionHandling.CustomExceptions.CityNotFoundException;
import com.weatherApp.common.exceptionHandling.CustomExceptions.DuplicateCityException;
import com.weatherApp.common.exceptionHandling.CustomExceptions.DuplicateUsernameException;
import com.weatherApp.common.exceptionHandling.CustomExceptions.InvalidCityException;
import com.weatherApp.common.exceptionHandling.CustomExceptions.InvalidPasswordException;
import com.weatherApp.common.exceptionHandling.CustomExceptions.MissingDataException;
import com.weatherApp.common.exceptionHandling.CustomExceptions.UnauthorizedExcepiton;
import com.weatherApp.common.exceptionHandling.CustomExceptions.WeatherApiException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor

public class GlobalExceptionHandler {
	
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	
	private final ErrorResponseBuilder builder;
	
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public Object handleMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpServletRequest request, HttpServletResponse response ) {
		
	    String uri = request.getRequestURI();
	    String message = "HTTP method not supported for this endpoint";
	    
	    if(uri.contains("/api")) {
	    	return builder.buildErrorResponse(message, HttpStatus.METHOD_NOT_ALLOWED);
	    }else {
	        request.setAttribute("errorMessage", message);
	        return "error/500"; 
	    }
	}
	
	
	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<?> handleIllegalArguments(IllegalArgumentException ex){
		
		return builder.buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
		
	}
	
	
	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<?> handleNoResourceFound(NoResourceFoundException ex, HttpServletResponse response, HttpServletRequest request) throws IOException, ServletException {
        
        String uri = request.getRequestURI();

		request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpServletResponse.SC_NOT_FOUND);
        request.setAttribute(RequestDispatcher.ERROR_MESSAGE, "No Resource Found");
        request.setAttribute(RequestDispatcher.ERROR_REQUEST_URI, uri);
        request.getRequestDispatcher("/error").forward(request, response);
		return builder.buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
	
	@ExceptionHandler(AuthorizationDeniedException.class)
	public ResponseEntity<Map<String, Object>> handleAuthorizationDenied(AuthorizationDeniedException ex) {
	    return builder.buildErrorResponse("You don't have the authority to access this resource", HttpStatus.FORBIDDEN);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleValidationErrors(MethodArgumentNotValidException ex){
		
		Map<String, Object> errorResponse = new HashMap<>(); 
		
		Map<String, Object> fieldErrors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error ->
					fieldErrors.put(error.getField(), error.getDefaultMessage())
				);
		
		errorResponse.put("timeStamp", LocalDateTime.now().format(formatter).toString());	
		errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
		errorResponse.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
		errorResponse.put("message", "Validation Failed");
		errorResponse.put("Details", fieldErrors);
		
		return ResponseEntity
				.status(HttpStatus.BAD_REQUEST)
				.body(errorResponse);
		
		
	}
	
	@ExceptionHandler(InvalidCityException.class)
	public ResponseEntity<?> handleInvalidCityErros(InvalidCityException ex){
		
		return builder.buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
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
		
		return builder.buildErrorResponse(message, HttpStatus.BAD_GATEWAY);
	}
	@ExceptionHandler(WeatherApiException.class)
	public ResponseEntity<?> handleWeatherApiError(WeatherApiException ex){
		
		return builder.buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		
		
	}
	
	@ExceptionHandler(CityNotFoundException.class)
	public ResponseEntity<?> handleCityNotFound(CityNotFoundException ex){
		return builder.buildErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
		
	}
	
	@ExceptionHandler(MissingDataException.class)
	public ResponseEntity<?> handleMissingData(MissingDataException ex){
		
		return builder.buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
		
	}
	
		
	@ExceptionHandler({DuplicateUsernameException.class,DuplicateCityException.class})
	public ResponseEntity<?> handleDuplicateResource(RuntimeException ex){
		
		return builder.buildErrorResponse(ex.getMessage(), HttpStatus.CONFLICT);
	}
	
	
//	@ExceptionHandler(AccessDeniedException.class)
//	public ResponseEntity<?> handleAccessDenials(AccessDeniedException ex){
//		
//		return buildErrorResponse(ex.getMessage(), HttpStatus.FORBIDDEN);
//		
//		
//	}
	
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<?> handleBadCredentials(BadCredentialsException ex){
		
		return builder.buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
		
		
	}
	
	@ExceptionHandler(InvalidPasswordException.class)
	public ResponseEntity<?> handleInvalidPassword(RuntimeException ex){
		
		return builder.buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
	}
	
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> handleGenericException (Exception ex){
		
		return builder.buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
}
