package com.severett.kotlinintro.controller;

import com.severett.kotlinintro.exception.EntityNotFoundException;
import com.severett.kotlinintro.exception.InternalException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class ControllerAdvisor {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleEntityNotFoundException(EntityNotFoundException enfe, WebRequest webRequest) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(InternalException.class)
    public ResponseEntity<Object> handleInternalException(InternalException ie, WebRequest webRequest) {
        return ResponseEntity.internalServerError().build();
    }
}
