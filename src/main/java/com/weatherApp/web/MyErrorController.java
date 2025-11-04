package com.weatherApp.web;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/error")
@Slf4j
public class MyErrorController implements ErrorController {


	@RequestMapping
	public Object handleWebErrors(HttpServletRequest request) {
		 Object statusObj = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
	        int status = statusObj != null ? Integer.parseInt(statusObj.toString()) : 500;
	        String uri = (String) request.getAttribute(RequestDispatcher.ERROR_REQUEST_URI);
	        String message = (String) request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
	        log.info("[ERROR CONTROLLER] ------------> status={}, uri={}, message = {}", status, uri,message);
	        return switch (status) {
	        case 401 -> "error/401";
	        case 403 -> "error/403";
	        case 404 -> "error/500";
            case 500 -> "error/500";
            default -> "error/error";
        };
	}
}
