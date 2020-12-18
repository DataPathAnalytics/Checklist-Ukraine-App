package com.datapath.checklistukraineapp.controller;

import com.datapath.checklistukraineapp.domain.response.ExceptionResponse;
import com.datapath.checklistukraineapp.exception.DepartmentException;
import com.datapath.checklistukraineapp.exception.UserException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestController
@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(value = {
            UserException.class,
            DepartmentException.class})
    public ResponseEntity<ExceptionResponse> exception(Exception ex) {
        ExceptionResponse response = new ExceptionResponse(ex.getMessage());
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
