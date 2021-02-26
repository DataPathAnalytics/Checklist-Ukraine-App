package com.datapath.checklistapp.controller.exception;

import com.datapath.checklistapp.dto.response.exception.ExceptionResponse;
import com.datapath.checklistapp.exception.*;
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
            EntityNotFoundException.class,
            UserException.class,
            MailException.class,
            ResetPasswordException.class,
            PermissionException.class,
            ValidationException.class})
    public ResponseEntity<ExceptionResponse> exception(Exception ex) {
        ExceptionResponse response = new ExceptionResponse(ex.getMessage());
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {UnmodifiedException.class})
    public ResponseEntity<ExceptionResponse> unmodifiedException(Exception ex) {
        ExceptionResponse response = new ExceptionResponse(ex.getMessage());
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.NOT_MODIFIED);
    }
}
