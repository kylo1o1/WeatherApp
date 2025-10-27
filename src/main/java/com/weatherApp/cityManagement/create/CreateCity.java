package com.weatherApp.cityManagement.create;


import java.util.List;

import org.springframework.stereotype.Service;
import com.weatherApp.cityManagement.City;
import com.weatherApp.cityManagement.CityRepo;
import com.weatherApp.common.exceptionHandling.CustomExceptions.DuplicateCityException;
import com.weatherApp.common.exceptionHandling.CustomExceptions.MissingDataException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateCity {


	private final CityRepo cityRepo;

	public CreateCityResponseDTO execute(CreateCityRequestDTO requestDTO) {
		
		if(!requestDTO.isValid() ) {
			throw new MissingDataException("City details are  required!");
		}
		
		String cityName = requestDTO.getName().trim();
		String country = requestDTO.getCountry().trim();
		String countryCode = requestDTO.getCountryCode().trim().toUpperCase();
		String timeZone = requestDTO.getTimeZone().trim();
		Double latitude = requestDTO.getLatitude();
		Double longitude = requestDTO.getLongitude();
		
		if( cityRepo.findByName(cityName).isPresent() ) {
			throw new DuplicateCityException("City " + cityName + " in Country " + country + "  already exists");
		}
		
		
		
		City newCity = new City();
		
		newCity.setName(cityName);
		newCity.setCountry(country);
		newCity.setCountryCode(countryCode);
		newCity.setLatitude(latitude);
		
		newCity.setLongitude(longitude);
		
		newCity.setTimeZone(timeZone);
		
		City savedCity = cityRepo.save(newCity);
		
		CreateCityResponseDTO response = new CreateCityResponseDTO(
				savedCity.getId(), 
				savedCity.getName(), 
				savedCity.getCountry(), 
				savedCity.getCountryCode(), 
				savedCity.getTimeZone(), 
				savedCity.getLongitude(), 
				savedCity.getLatitude(), 
				"City has been added");
		
				
		
		return response;
		
		
	}
	
	
	public void addMultipleCities(List<CreateCityRequestDTO> requests) {
		
		for (CreateCityRequestDTO request : requests) {
			
			try {
				
				execute(request);
				
			} catch (Exception e) {
				
				log.error("Failed to add City : "+ request.getName() + "   Cause :  " + e.getMessage());
				
			}
			
		}
		
	}
	
}
