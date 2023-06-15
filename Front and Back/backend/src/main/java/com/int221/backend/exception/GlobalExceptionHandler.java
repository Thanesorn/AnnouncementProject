package com.int221.backend.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        List<Map<String, String>> validationErrors = new ArrayList<>();

        List<ObjectError> globalErrors = ex.getBindingResult().getGlobalErrors();
        for (ObjectError globalError : globalErrors) {
            String errorMessage = globalError.getDefaultMessage();
            Map<String, String> validationError = new HashMap<>();
            validationError.put("field", "");
            validationError.put("errorMessage", errorMessage);
            validationErrors.add(validationError);
        }

        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            String field = fieldError.getField();
            String errorMessage = fieldError.getDefaultMessage();
            Map<String, String> validationError = new HashMap<>();
            validationError.put("field", field);
            validationError.put("errorMessage", errorMessage);
            validationErrors.add(validationError);
        }

        Map<String, List<Map<String, String>>> response = new HashMap<>();
        response.put("detail", validationErrors);

        return ResponseEntity.badRequest().body(response);
    }
}
