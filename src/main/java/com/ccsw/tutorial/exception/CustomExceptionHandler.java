package com.ccsw.tutorial.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(ClientCantLoanException.class)
    public ResponseEntity<?> handleClientCantLoanException(ClientCantLoanException ccl, WebRequest request) {
        Map<String, String> errorResponseBody = new HashMap<>();
        errorResponseBody.put("message", ccl.getMessage());
        return new ResponseEntity<>(errorResponseBody, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(GameAlreadyLoanedException.class)
    public ResponseEntity<?> handleGameAlreadyLoanedException(GameAlreadyLoanedException gal) {
        Map<String, String> errors = new HashMap<>();
        errors.put("message", gal.getMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
