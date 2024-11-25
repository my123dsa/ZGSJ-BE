package com.example.Finance.error;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(FeignException.class)
    private ResponseEntity<ErrorDTO> handleFeignException(FeignException e) {
        try {
            return ResponseEntity
                    .status(500)
                    .body(ErrorDTO.builder()
                            .code("FEIGN_ERROR")
                            .message(e.getMessage())
                            .build());
        } catch (Exception ex) {
            return ResponseEntity
                    .status(500)
                    .body(ErrorDTO.builder()
                            .code("FEIGN_EXCEPTION_ERROR")
                            .message("EXCEPTION_ERROR")
                            .build());
        }
    }

    @ExceptionHandler({CustomException.class})
    protected ResponseEntity<ErrorDTO> handleCustomException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    private ResponseEntity<ErrorDTO> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getStatus())
                .body(makeErrorResponse(errorCode));
    }

    private ErrorDTO makeErrorResponse(ErrorCode errorCode) {
        return ErrorDTO.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();
    }
}
