package com.weatherApp.common.exceptionHandling;


public class CustomExceptions {

	public static class DuplicateUsernameException extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public DuplicateUsernameException(String message) {
			
			super(message);
			
		}
		
	}
	public static class DuplicateCityException extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public DuplicateCityException(String message) {
			
			super(message);
			
		}
		
	}
	
	public static class MissingDataException extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public MissingDataException(String message) {
			// TODO Auto-generated constructor stub
			
			super(message);
		}
	}
	
	public static class CityNotFoundException extends RuntimeException{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public CityNotFoundException(String message) {
			
			super(message);
			
		}
		
	}
	
	public static class WeatherApiException extends RuntimeException{
		
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public WeatherApiException(String message) {
			// TODO Auto-generated constructor stub
			super(message);
		}
	}
}
	
