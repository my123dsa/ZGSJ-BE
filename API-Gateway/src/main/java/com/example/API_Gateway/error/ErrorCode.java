package com.example.API_Gateway.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    INVALID_ENCRYPTION(401,"암호화가 유효하지 않습니다."),
    SERVER_ERROR(500, "서버 에러입니다. 서버 팀에 연락주세요!"),
    INVALID_DECRYPTION(401,"복호화가 유효하지 않습니다.");


    private final int status;
    private final String message;
}
