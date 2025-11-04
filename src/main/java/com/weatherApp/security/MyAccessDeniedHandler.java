package com.weatherApp.security;

import java.io.IOException;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weatherApp.common.exceptionHandling.ErrorResponseBuilder;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class MyAccessDeniedHandler  implements AccessDeniedHandler{

	private final ErrorResponseBuilder builder;
	private final ObjectMapper mapper;
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
	                   AccessDeniedException accessDeniedException) throws IOException, ServletException {

	    String uri = request.getRequestURI();
	    
	    log.info("urriii-> {}", uri);
	    if(uri.contains("/api")) {
	    
	    	ResponseEntity<Map<String, Object>> entity = builder.buildErrorResponse("Unauthorized",HttpStatus.UNAUTHORIZED);
	    	response.setStatus(entity.getStatusCode().value());
	    	response.setContentType("application/json");
	    	response.getWriter().write(mapper.writeValueAsString(entity.getBody()));	    	
	    }else {
	    	request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpServletResponse.SC_FORBIDDEN);
            request.setAttribute(RequestDispatcher.ERROR_MESSAGE, "Unauthorized access");
            request.setAttribute(RequestDispatcher.ERROR_REQUEST_URI, uri);
            request.getRequestDispatcher("/error").forward(request, response);
	    }
	    
	    
	}


	
	
}
