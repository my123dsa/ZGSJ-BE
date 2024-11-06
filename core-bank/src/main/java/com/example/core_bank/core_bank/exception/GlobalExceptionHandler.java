package com.example.core_bank.core_bank.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    //이 곳에서, 만든 Exception을 처리합니다.
//    @ExceptionHandler(CustomException.class)
//    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
//        log.error("CustomException: {}", e.getMessage(), e);
//        ErrorResponse response = new ErrorResponse(e.getErrorCode(), e.getMessage());
//        return new ResponseEntity<>(response, e.getErrorCode().getStatus());
//    }

}
