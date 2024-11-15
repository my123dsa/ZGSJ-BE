package com.example.API_Gateway.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    BAD_GATEWAY_TEST(HttpStatus.BAD_GATEWAY,"이거 바꿔야함"),
    INVALID_ENCRYPTION(HttpStatus.FORBIDDEN,"암호화가 유효하지 않습니다."),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러입니다. 서버 팀에 연락주세요!"),
    INVALID_DECRYPTION(HttpStatus.FORBIDDEN,"복호화가 유효하지 않습니다."),
    UNACCEPT_TOKEN(HttpStatus.UNAUTHORIZED, "Token is null or too short"),
    BADTYPE_BEARER(HttpStatus.UNAUTHORIZED, "Token type Bearer"),
    MALFORM_TOKEN(HttpStatus.FORBIDDEN, "Malformed Token"),
    BADSIGN_TOKEN(HttpStatus.FORBIDDEN, "BadSignatured Token"),
    EXPIRED_TOKEN(HttpStatus.FORBIDDEN, "Expired Token")
    ;

    private final HttpStatus status;
    private final String message;
}
