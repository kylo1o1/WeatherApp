package com.weatherApp.cityManagement;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;



@Repository
public interface CityRepo extends JpaRepository<City, Long> {

	
	Optional<City>  findByName(String name);
	
	Optional<City> findByCountryCode(String countryCode);
	
	
	
}
