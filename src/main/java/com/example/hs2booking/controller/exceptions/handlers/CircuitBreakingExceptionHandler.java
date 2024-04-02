package com.example.hs2booking.controller.exceptions.handlers;

import com.example.hs2booking.controller.exceptions.ControllerException;
import com.example.hs2booking.controller.exceptions.fallback.DubControllerException;
import com.example.hs2booking.controller.exceptions.fallback.ServiceUnavailableException;
import com.example.hs2booking.model.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;



@ControllerAdvice
public class CircuitBreakingExceptionHandler {

    @ExceptionHandler(ServiceUnavailableException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    protected ErrorDTO handleServiceUnavailability(ControllerException ex) {
        return new ErrorDTO(
                ex.getTimestamp(),
                ex.getMessage(),
                ex.getError()
        );
    }

    @ExceptionHandler(DubControllerException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    protected ErrorDTO handleServiceException(ControllerException ex) {
        return new ErrorDTO(
                ex.getTimestamp(),
                ex.getMessage(),
                ex.getError()
        );
    }
}
