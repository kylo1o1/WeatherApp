package com.weatherApp.cityManagement.delete;

import org.springframework.stereotype.Service;

import com.weatherApp.cityManagement.City;
import com.weatherApp.cityManagement.CityRepo;
import com.weatherApp.common.exceptionHandling.CustomExceptions.CityNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RemoveCity {

	private final  CityRepo cityRepo;
	
	
	public RemovedCityResponse execute(Long id) {
		
		if(id == null) {
			throw new RuntimeException("City ID is required!");
		}
		
		City city = cityRepo.findById(id)
				.orElseThrow(() -> new RuntimeException("No City Found with id: " + id)); 
		
		String cityName = city.getName();
		
		cityRepo.deleteById(id);
		
		return new RemovedCityResponse(
				"City removed Successfully",
				cityName
				);
		
	}
	
	public RemovedCityResponse execute(String name) {
		if(name.isEmpty()) {
			throw new IllegalArgumentException("City name is required!");
		}
		
		City city = cityRepo.findByName(name)
				.orElseThrow(() -> new CityNotFoundException("No City Found with name: " + name)); 
		
		String cityName = city.getName();
		
		cityRepo.deleteById(city.getId());
		
		return new RemovedCityResponse(
				"City removed Successfully",
				cityName
				);
		
	}
	
	
}
