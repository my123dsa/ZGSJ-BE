package com.example.Attendance.error;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolation(
            ConstraintViolationException ex) {
        ErrorCode errorCode = ErrorCode.WRONG_BODY;
        return handleExceptionInternal(errorCode);
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
