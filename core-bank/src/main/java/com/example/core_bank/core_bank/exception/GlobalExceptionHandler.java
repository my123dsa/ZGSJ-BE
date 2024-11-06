package com.example.core_bank.core_bank.exception;

import com.example.core_bank.core_bank.dto.businessnumber.BusinessNumberResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessNumberException.class)
    public ResponseEntity<BusinessNumberResponse> handleBusinessNumberException(BusinessNumberException e) {
        BusinessNumberResponse errorResponse = new BusinessNumberResponse(
                false,  // exists
                true,   // hasError
                "사업자 정보 확인 중 오류 발생!" // message
        );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }

}
