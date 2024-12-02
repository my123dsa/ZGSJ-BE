package com.example.Attendance.error;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class FeignExceptionHandler {
    public final ObjectMapper objectMapper;

    public ErrorDTO feToErrorDTO(FeignException fe){
        try {
            return objectMapper.readValue(fe.contentUTF8(), ErrorDTO.class);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.ERROR_NOT_FOUND);
        }
    }
}
