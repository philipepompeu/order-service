package com.github.philipepompeu.order_service.controllers;

import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.github.philipepompeu.order_service.infra.auth.AuthFailureException;

import jakarta.persistence.EntityNotFoundException;

@ControllerAdvice
public class ControllerExceptionHandler {


    @ExceptionHandler(EntityNotFoundException.class)    
    public ResponseEntity<ProblemDetail> handleEntityNotFoundException(EntityNotFoundException ex) {
        ProblemDetail problemDetail = createProblemDetail(HttpStatus.UNPROCESSABLE_ENTITY, "Entity Not Found", ex.getMessage());        
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(problemDetail);
    }
    
    @ExceptionHandler(AuthFailureException.class)    
    public ResponseEntity<ProblemDetail> handleFailedLoginException(AuthFailureException ex) {
        ProblemDetail problemDetail = createProblemDetail(HttpStatus.UNAUTHORIZED, "Authorization Failed", ex.getMessage());        

        System.out.println(String.format("Auth failed [%s] - %s", ex.getClass().getName(), ex.getMessage()));

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problemDetail);
    }

    @ExceptionHandler(Exception.class)    
    public ResponseEntity<ProblemDetail> handleGenericException(Exception ex) {
        ProblemDetail problemDetail = createProblemDetail(HttpStatus.INTERNAL_SERVER_ERROR,"Internal Server Error","An unexpected error occurred");

        System.out.println(String.format("Internal Error[%s] - %s", ex.getClass().getName(), ex.getMessage()));

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
                
    }

    private ProblemDetail createProblemDetail(HttpStatus status, String title, String detail) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(status);
        problemDetail.setTitle(title);
        problemDetail.setDetail(detail);
        problemDetail.setProperty("timestamp", LocalDateTime.now());
        return problemDetail;
    
}
