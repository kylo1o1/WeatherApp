package com.weatherApp.cityManagement;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.weatherApp.cityManagement.create.CreateCity;
import com.weatherApp.cityManagement.create.CreateCityRequestDTO;
import com.weatherApp.cityManagement.create.CreateCityResponseDTO;
import com.weatherApp.cityManagement.delete.RemoveCity;
import com.weatherApp.cityManagement.delete.RemovedCityResponse;
import com.weatherApp.cityManagement.retrieve.CityInfoDTO;
import com.weatherApp.cityManagement.retrieve.CityResponseDTO;
import com.weatherApp.cityManagement.retrieve.GetCities;
import com.weatherApp.common.exceptionHandling.CustomExceptions.CityNotFoundException;
import com.weatherApp.common.exceptionHandling.CustomExceptions.DuplicateCityException;
import com.weatherApp.common.exceptionHandling.CustomExceptions.MissingDataException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/city")
public class CityController {

	private final CreateCity createCity;
	private final GetCities getCities;
	private final RemoveCity removeCity;
	
	@GetMapping("/all")
	public ResponseEntity<?> getAllCities(){
		
			CityResponseDTO response = getCities.execute();
			
			return ResponseEntity.ok(response);
		
	}
	
	@GetMapping("/{name}")
	public ResponseEntity<?> getCityByName(@PathVariable String name){
		
		
	
			
			CityInfoDTO response = getCities.getCityByName(name);
			
			return ResponseEntity
					.ok(response);
			
		
	}
	
	@GetMapping("/country/{countryCode}")
	public ResponseEntity<?> getCityByCountry(@PathVariable String countryCode){
		
		
			
			CityResponseDTO response = getCities.getCitiesByCountry(countryCode);
			
			return ResponseEntity.ok(response);
			
		
		
	}
	
	
	
	
	@PostMapping("/addCity")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> addCity(@RequestBody CreateCityRequestDTO cityRequestDTO){
		
	
			
			CreateCityResponseDTO response = createCity.execute(cityRequestDTO);
			
			return ResponseEntity.ok(response);
			
			
		
		
	}
	
	@PostMapping("/addMultipleCity")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> addMultipleCity(@RequestBody List<CreateCityRequestDTO> cityLists){
		
		
			
			  createCity.addMultipleCities(cityLists);
			
			  return ResponseEntity.ok("Cities has been Added");
			  
		
		
	}
	
	
//	@DeleteMapping("/{id}")
//	@PreAuthorize("hasRole('ADMIN')")
//	public ResponseEntity<?> removeCityById(@PathVariable Long id){
//		
//		try {
//			
//			RemovedCityResponse response = removeCity.removeCity(id);
//			
//			return ResponseEntity.ok(response);
//			
//		} catch (Exception e) {
//			return ResponseEntity
//					.status(HttpStatus.BAD_GATEWAY)
//					.body(e.getMessage());
//		}
//	}
	
	
	@DeleteMapping("/{name}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> removeCity(@PathVariable String name){
		
			RemovedCityResponse response = removeCity.execute(name);
			
			return ResponseEntity.ok(response);
			
		
	}
	
	
	
}
