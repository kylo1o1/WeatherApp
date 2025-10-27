package com.weatherApp.cityManagement.retrieve;

import java.util.List;

import org.springframework.stereotype.Service;

import com.weatherApp.cityManagement.City;
import com.weatherApp.cityManagement.CityRepo;
import com.weatherApp.common.exceptionHandling.CustomExceptions.CityNotFoundException;
import com.weatherApp.common.exceptionHandling.CustomExceptions.MissingDataException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetCities {

	
	private final CityRepo cityRepo;
	
	public CityResponseDTO execute() {
		
		List<City> cities = cityRepo.findAll();
		
		List<CityInfoDTO> cityinfo = cities.stream()
				.map(city -> new CityInfoDTO(
						city.getId(), 
						city.getName(), 
						city.getCountry(), 
						city.getCountryCode(), 
						city.getTimeZone(),  
						city.getLongitude(),
						city.getLatitude()
						))
				.toList();
		
		CityResponseDTO response = new CityResponseDTO();
		
		response.setCities(cityinfo);
		response.setTotalCount(cityinfo.size());
		return response;
		
	}
	
	public CityInfoDTO getCityByName(String name) {
		if(name.isEmpty()) {
			throw new MissingDataException("City Name is Required!");

		}
		
		
		City city = cityRepo.findByName(name.trim()).
				orElseThrow(() -> new CityNotFoundException("No city Found with that name"));
				
				
		
		CityInfoDTO cityInfoDTO = new CityInfoDTO();
		cityInfoDTO.setId(city.getId()); 
		cityInfoDTO.setName(city.getName()); 
		cityInfoDTO.setCountry(city.getCountry()); 
		cityInfoDTO.setCountryCode(city.getCountryCode());
		cityInfoDTO.setTimeZone(city.getTimeZone());
		cityInfoDTO.setLongitude(city.getLongitude());
		cityInfoDTO.setLatitude(city.getLongitude());
		
		return cityInfoDTO;
		
		
	}
	
	
	public CityResponseDTO getCitiesByCountry(String countryCode) {
		
		List<CityInfoDTO> cityInfoDTOs = cityRepo.findAll().stream()
				.filter(city -> city.getCountryCode().equalsIgnoreCase(countryCode))
				.map(city -> new CityInfoDTO(
						city.getId(),
						city.getName(),
						city.getCountry(),
						city.getCountryCode(),
						city.getTimeZone(),
						city.getLongitude(),
						city.getLatitude()
						)
						)
				.toList();
		
		CityResponseDTO response = new CityResponseDTO();
		
		response.setCities(cityInfoDTOs);
		response.setTotalCount(cityInfoDTOs.size());
		
		return response;
	}
}
