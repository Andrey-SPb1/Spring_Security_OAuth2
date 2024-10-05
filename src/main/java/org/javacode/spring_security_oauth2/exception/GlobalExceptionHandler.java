package org.javacode.spring_security_oauth2.exception;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ModelAndView handleBadCredentialsException(BadCredentialsException ex) {
        ModelAndView mav = new ModelAndView("errors/authentication-error");
        mav.addObject("error", "Incorrect username or password. " + ex.getMessage());
        return mav;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDeniedException(AccessDeniedException ex) {
        ModelAndView mav = new ModelAndView("errors/access-denied");
        mav.addObject("error", "Insufficient access rights. " + ex.getMessage());
        return mav;
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ModelAndView handleTokenExpiredException(TokenExpiredException ex) {
        ModelAndView mav = new ModelAndView("errors/token-expired");
        mav.addObject("error", "Token expired " + ex.getMessage());
        return mav;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGenericException(Exception ex) {
        ModelAndView mav = new ModelAndView("errors/error");
        mav.addObject("error", "Error: " + ex.getMessage());
        return mav;
    }

}
