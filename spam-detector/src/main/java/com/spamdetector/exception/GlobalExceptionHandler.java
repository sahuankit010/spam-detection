package com.spamdetector.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicateSpamReportException.class)
    public ResponseEntity<String> handleDuplicateSpamReportException(DuplicateSpamReportException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }


}
