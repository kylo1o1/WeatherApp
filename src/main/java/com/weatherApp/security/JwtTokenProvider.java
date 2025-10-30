package com.weatherApp.security;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class JwtTokenProvider {

	
	private static final String SECRET = "2c5f15fb8a8a115dfcfd9fef892e62a020cd02b626a2d95692a833d52ec80fbc";
	
	private static final long EXPIRATION_TIME = 1000 * 60 * 60 *10;
	
	private SecretKey getSignKey() {
		byte[] keyBytes = Decoders.BASE64.decode(SECRET);
		return Keys.hmacShaKeyFor(keyBytes);
	}
	
	
	public String extractUserName(String token) {
		
		return extractClaim(token,Claims::getSubject);
		
	}
	
	
	public Date extractExpiration(String token) {
		return extractClaim(token,Claims::getExpiration);
	}


	private <T> T extractClaim(String token,Function<Claims, T> claimsResolver) {

		final Claims claims = extractAllClaims(token);
		return claimsResolver.apply(claims);
	}
	
	
	private Claims extractAllClaims(String token) {
		
		return Jwts.parser()
				.verifyWith((SecretKey) getSignKey())
				.build()
				.parseSignedClaims(token)
				.getPayload()
				;
				
		
	}
	
	private boolean isExpired(String token) {
		return extractExpiration(token).before(new Date());
		
	}
	
	
	
	public String generateToken(String username,String role) {
		Map<String, Object> claims = new HashMap<>();
		
		claims.put("role", role);
		return createToken(claims,username);
	}
	
	private String createToken(Map<String, Object> claims, String subject) {
		
		long now = System.currentTimeMillis();
		
		return Jwts.builder()
				.claims(claims)
				.subject(subject)
				.issuedAt(new Date(now))
				.expiration(new Date(now + EXPIRATION_TIME))
				.signWith(getSignKey(),Jwts.SIG.HS256)
				.compact();
		
	}
	
	public boolean validateToken(String token, String username) {

		final String extractedUsername = extractUserName(token);

		return (extractedUsername.equals(username) && !isExpired(token));
		
		
	}
	
	
	public String extractRole (String token) {
		
		Claims claims = extractAllClaims(token);
		return claims.get("role",String.class);
		
	}
	
	
}
