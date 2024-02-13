package com.lql.userservice.exception.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.support.WebExchangeBindException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@ControllerAdvice
@ResponseBody
public class ValidationExceptionHandling {

    @ExceptionHandler(WebExchangeBindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> validationErrorException(WebExchangeBindException e) {

        Map<String, String> error = new ConcurrentHashMap<>();
        e.getBindingResult().getFieldErrors().forEach(field -> error.put(field.getField(), field.getDefaultMessage()));
        return error;
    }
}
