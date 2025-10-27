package com.weatherApp.cityManagement.retrieve;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CityResponseDTO {

	private List<CityInfoDTO> cities;
	private int totalCount;
	
	
	
}
