package com.weatherApp.authentication;


import com.weatherApp.security.Role;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	
	private String username;
	
	private String email;
	private String password;
	
	@Enumerated(EnumType.STRING)
	private Role role;
	
	
	
	public boolean isAdmin() {
		return this.role == Role.ADMIN;
	}
	
	public boolean isRegularUser() {
		return this.role == Role.USER;
	}
}
