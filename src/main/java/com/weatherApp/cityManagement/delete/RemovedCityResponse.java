package com.weatherApp.cityManagement.delete;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemovedCityResponse {

	private String message;
	private String cityName;
}
