package com.weatherApp.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtTokenProvider jwtTokenProvider;
	
	

	@Override
	protected void doFilterInternal(
			HttpServletRequest request, 
			HttpServletResponse response, 
			FilterChain filterChain)
			throws ServletException, IOException {
		
		String authHeader = request.getHeader("Authorization");
		String token = null;
		String username = null;
		
		
		if(authHeader != null && authHeader.startsWith("Bearer ")) {
			
			token = authHeader.substring(7);
			
		}else {
			
			Object sessionToken = request.getSession(false) != null ? 
					request.getSession().getAttribute("token") :null;
			if(sessionToken != null) {
				
				token = sessionToken.toString();
			}
		}
		
		
		if (token != null) {
            try {
                username = jwtTokenProvider.extractUserName(token);
            } catch (Exception e) {
                log.error("Invalid JWT token: {}", e.getMessage());
            }
        }
		
		
		if(username !=null && SecurityContextHolder.getContext().getAuthentication() == null) {
			if(jwtTokenProvider.validateToken(token, username)) {
				
				String role = jwtTokenProvider.extractRole(token);

				SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_"+role);
			
				UsernamePasswordAuthenticationToken authToken = 
						new UsernamePasswordAuthenticationToken(
								username, 
								null,
								Collections.singleton(authority)
								);
				authToken.setDetails(
						new WebAuthenticationDetailsSource().buildDetails(request)
						);
				
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
			
		}
		
		filterChain.doFilter(request, response);
	}
}
