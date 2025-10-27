package com.weatherApp.common.Aspect.util;

import java.util.Arrays;

public class LogSanitize {

	
	public static Object sanitize (Object [] args) {
		if(args == null) return "[]";
		
		return Arrays.stream(args)
				.map(LogSanitize::maskObject)
				.toList();
	}
	
	public static Object maskObject(Object obj) {
		if(obj == null) return null;
		
		if(obj instanceof String s) {
			if(s.toLowerCase().contains("token")) return "MASKED_TOKEN";
			if(s.toLowerCase().contains("password")) return "MASKED_PASSWORD";
		}
		
		return obj;
	}
	
}
