package com.eric.exception.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description:
 * @Author: Eric Liang
 * @Since: 2020-5-26 19:06
 */
@ControllerAdvice
public class GlobalDefaultExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public void defaultErrorHandler(HttpServletRequest req, Exception e)  {
        e.printStackTrace();
        System.out.println("GlobalDefaultExceptionHandler.defaultErrorHandler()");
    }
}
