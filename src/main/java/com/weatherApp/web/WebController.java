package com.weatherApp.web;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.weatherApp.authentication.login.Login;
import com.weatherApp.authentication.login.LoginRequestDTO;
import com.weatherApp.authentication.login.LoginResponseDTO;
import com.weatherApp.authentication.signUp.Request;
import com.weatherApp.authentication.signUp.SignUp;
import com.weatherApp.cityManagement.create.CreateCity;
import com.weatherApp.cityManagement.create.CreateCityRequestDTO;
import com.weatherApp.cityManagement.create.CreateCityResponseDTO;
import com.weatherApp.cityManagement.delete.RemoveCity;
import com.weatherApp.cityManagement.retrieve.CityResponseDTO;
import com.weatherApp.cityManagement.retrieve.GetCities;
import com.weatherApp.weatherFetching.FetchWeatherForCity;
import com.weatherApp.weatherFetching.DTO.WeatherRequestDTO;
import com.weatherApp.weatherFetching.DTO.WeatherResponseDTO;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebController {

	private final SignUp signUpUseCase;
	private final Login loginUseCase;
	
	private final GetCities citiesUseCase;
	
	private final CreateCity createCity;
	
	private final RemoveCity removeCity;
	private final FetchWeatherForCity weatherForCity;
    
	@GetMapping("/")
    public String index() {
        return "index";  
    }

   @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

	@PostMapping("/login")
	public String handleLogin(
			LoginRequestDTO request,
			HttpSession session,
			RedirectAttributes redirectAttributes
			) {
		
		try {
			
			
			LoginResponseDTO response = loginUseCase.execute(request);
			
			session.setAttribute("token", response.getToken());
			session.setAttribute("username", response.getUsername());
			session.setAttribute("role", response.getRole());
			
			log.info("User {} logged in successfully" , request.getUsername());
			
			if("ADMIN".equals(response.getRole())) {
				return "redirect:/admin";
			}else {
				return "redirect:/dashboard";
			}
			
			
		} catch (Exception e) {
			log.error("Login failed for user {} : {}" , request.getUsername(),e.getMessage());
			redirectAttributes.addFlashAttribute("error",e.getMessage());
			return "redirect:/login";
		}
		
	}
	
	@GetMapping("/signup")
	public String showSignUp() {
		return "signUp";
	}

	@PostMapping("/signup")
	public String handleSignUp(
			@Valid @ModelAttribute Request request, 
			BindingResult result,
			RedirectAttributes redirectAttributes
			
			) {
		
		 if (result.hasErrors()) {
		        redirectAttributes.addFlashAttribute("error", result.getAllErrors().get(0).getDefaultMessage());
		        return "redirect:/signup";
		    }
		try {
			
			
			signUpUseCase.execute(request);
			
			redirectAttributes.addFlashAttribute("Success", "User Registered succesfully");
			
			
			return "redirect:/login";
		} catch (Exception e) {
			
			redirectAttributes.addFlashAttribute("error", e.getMessage());
			return "redirect:/signup";
		}
		
	}
	
	 @GetMapping("/dashboard")
    public String showDashboard(HttpSession session, Model model) {
        
		
        if (session.getAttribute("username") == null) {
            return "redirect:/login";
        }
        
        
        model.addAttribute("username", session.getAttribute("username"));
        model.addAttribute("role", session.getAttribute("role"));
        
       
        try {
            CityResponseDTO response = citiesUseCase.execute();
            model.addAttribute("cities", response.getCities());
        } catch (Exception e) {
            log.error("Error loading cities: {}", e.getMessage());
            model.addAttribute("error", "Failed to load cities");
        }
        
        return "dashboard"; 
    }
	 
	 @PostMapping("/weather")
	 public String  fetchWeather(
			 @RequestParam String cityName,
			 HttpSession session,
			 Model model,
			 RedirectAttributes redirectAttributes
			 ) {
		 
		 if(session.getAttribute("username") ==  null) {
			 return "redirect:/login";
			 
		 }
		 
		 try {
			
			 WeatherRequestDTO request = new WeatherRequestDTO(cityName);
			 
			 WeatherResponseDTO weather = weatherForCity.execute(request);
			 
			 model.addAttribute("weather", weather);
	            model.addAttribute("username", session.getAttribute("username"));
	            model.addAttribute("role", session.getAttribute("role"));
	         CityResponseDTO response = citiesUseCase.execute();
	         model.addAttribute("cities", response	.getCities());
			 
	         return "dashboard";
		} catch (Exception e) {
			// TODO: handle exception
			
			log.error("Error Fetching Weather : ", e.getMessage());
			redirectAttributes.addFlashAttribute("error: " , e.getMessage());
			return "redirect:/dashboard";
		}
		 
	 
	 }
	 
	 
	 @GetMapping("/admin")
	 public String showAdmin(HttpSession session,Model model) {
		 
		 if(session.getAttribute("username") == null) {
			 return "redirect:/login";
		 }
		 if(!"ADMIN".equals(session.getAttribute("role"))) {
			 
			 return "redirect:/dashboard";
		 }
		 
		 model.addAttribute("username", session.getAttribute("username"));
		 model.addAttribute("role",session.getAttribute("role"));
		 
		 try {
			
			 
			 
			 CityResponseDTO  cities = citiesUseCase.execute();
			 model.addAttribute("cities", cities.getCities());
			 
		} catch (Exception e) {
			 log.error("Error loading cities: {}", e.getMessage());
	         model.addAttribute("error", "Failed to load cities");
		}
		 
		 return "admin";
	 }
	 @PostMapping("/admin/add-city")
	 public String addCity(
			 @Valid @ModelAttribute CreateCityRequestDTO request,
			 BindingResult result,
			 HttpSession session,
			 Model model,
			 RedirectAttributes redirectAttributes
			 
			 ) {
		 
		 if(!session.getAttribute("role").equals("ADMIN")) {
			 return "redirect:/login";
		 }
		 if (result.hasErrors()) {
		        redirectAttributes.addFlashAttribute("error", result.getAllErrors().get(0).getDefaultMessage());
		        return "redirect:/signup";
		    }
		 
		 try {
			
			
			 
			 
			  createCity.execute(request);
			 
			 redirectAttributes.addFlashAttribute("success",
					 "City " + request.getName() + " added Successfulyy"
					 );
			 
			 return "redirect:/admin";
			 
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute("error",e.getMessage());
		}
		 
	        return "redirect:/admin";
	 }
	 
	 @PostMapping("admin/remove-city")
	 public String  removeCity(@RequestParam Long cityId,HttpSession session, RedirectAttributes redirectAttributes) {
		 
		 
		 if(!"ADMIN".equals(session.getAttribute("role"))) {
			 
			 return "redirect:/dashboard";
		 }
		 
		 try {
			
			 removeCity.execute(cityId);
			 redirectAttributes.addFlashAttribute("success","City Removed Successfully");
			 
			 
			 
		} catch (Exception e) {
			// TODO: handle exception
			redirectAttributes.addFlashAttribute("error",e.getMessage());
		}
		 
		return "redirect:/admin";
		 
	 }
	 
	 @GetMapping("/logout")
	    public String logout(HttpSession session) {
	        session.invalidate();
	        return "redirect:/login";
	    }
}
