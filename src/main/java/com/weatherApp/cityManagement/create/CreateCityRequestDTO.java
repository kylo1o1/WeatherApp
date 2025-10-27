package com.weatherApp.cityManagement.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CreateCityRequestDTO {

	
	private String name;
	private String country;
	private String countryCode;
	private String timeZone;
	private Double latitude;
	private Double longitude;
	
	public boolean isValid() {
        return name != null && !name.trim().isEmpty() 
            && country != null && !country.trim().isEmpty()
           
            && timeZone != null && latitude != null
            && longitude != null && countryCode !=null;
    }
}
