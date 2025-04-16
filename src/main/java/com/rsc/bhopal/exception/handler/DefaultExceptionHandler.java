package com.rsc.bhopal.exception.handler;

import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class DefaultExceptionHandler{

    @ExceptionHandler({ AuthenticationException.class })
    public String handleAuthenticationException(Exception ex) {  
    	return "error/403";
        
    }
}