package com.example.Finance.error;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Object> handleFeignException(FeignException e) {
        try {
            // Feign에서 받은 에러 응답(JSON)을 ErrorDTO로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            ErrorDTO errorDTO = objectMapper.readValue(e.contentUTF8(), ErrorDTO.class);
            return ResponseEntity.status(e.status())
                    .body(errorDTO);
        } catch (JsonProcessingException ex) {
            //변환 과정 다시 오류 발생 시 ,기본 값 전달
            return ResponseEntity.status(e.status())
                    .body(new ErrorDTO("FEIGN_ERROR", e.contentUTF8()));
        }
    }

    @ExceptionHandler({CustomException.class})
    protected ResponseEntity<?> handleCustomException(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        return handleExceptionInternal(errorCode);
    }

    private ResponseEntity<?> handleExceptionInternal(ErrorCode errorCode) {
        return ResponseEntity.status(errorCode.getStatus())
                .body(makeErrorResponse(errorCode));
    }

    private ErrorDTO makeErrorResponse(ErrorCode errorCode) {
        return ErrorDTO.builder()
                .code(errorCode.name())
                .message(errorCode.getMessage())
                .build();
    }

    @ExceptionHandler(ArithmeticException.class) // ③
    public ResponseEntity<Object> handleArithmeticException(ArithmeticException e) {
        ErrorCode errorCode = ErrorCode.SERVER_ERROR;
        return handleExceptionInternal(errorCode, e.getMessage());
    }


    private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, String message) {
        return ResponseEntity.status(errorCode.getStatus())
                .body(makeErrorResponse(errorCode, message));
    }

    private ErrorDTO makeErrorResponse(ErrorCode errorCode, String message) {
        return ErrorDTO.builder()
                .code(errorCode.name())
                .message(message)
                .build();
    }
}
