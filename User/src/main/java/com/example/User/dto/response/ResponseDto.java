package com.example.User.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ResponseDto {

    private String message;

    public static ResponseDto of(String message)
    {
        return new ResponseDto(message);
    }
}
