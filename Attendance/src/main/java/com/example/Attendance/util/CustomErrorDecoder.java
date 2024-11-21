package com.example.Attendance.util;

import com.example.Attendance.error.CustomException;
import com.example.Attendance.error.ErrorCode;
import com.example.Attendance.error.ErrorDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomErrorDecoder implements ErrorDecoder {
    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        try {
            ErrorDTO errorDTO = objectMapper.readValue(response.body().asInputStream(), ErrorDTO.class);
            try {
                ErrorCode errorCode = ErrorCode.valueOf(errorDTO.getCode());
                return new CustomException(errorCode);
            } catch (IllegalArgumentException e) {
                // 일치하는 ErrorCode를 찾지 못한 경우
                return new CustomException(ErrorCode.SERVER_ERROR);
            }
        } catch (IOException e) {
            return new FeignException.FeignServerException(response.status(), "Error parsing error response", response.request(), null, null);
        }
    }
}