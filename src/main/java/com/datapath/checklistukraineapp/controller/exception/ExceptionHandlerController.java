package com.datapath.checklistukraineapp.controller.exception;

import com.datapath.checklistukraineapp.dto.response.ExceptionResponse;
import com.datapath.checklistukraineapp.exception.*;
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
            DepartmentException.class,
            MailException.class,
            ResetPasswordException.class,
            PermissionException.class,
            TemplateException.class,
            FolderException.class,
            QuestionException.class,
            QuestionSourceException.class,
            ControlObjectException.class,
            ControlTypeException.class,
            ControlStatusException.class,
            ControlEventException.class})
    public ResponseEntity<ExceptionResponse> exception(Exception ex) {
        ExceptionResponse response = new ExceptionResponse(ex.getMessage());
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }
}
