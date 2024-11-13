package com.example.Finance.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    INVALID_PARAMETER(400, "파라미터 값을 확인해주세요."),
    INVALID_EMAIL_FORMAT(431, "이메일 형식 잘못되었습니다."),
    EMAIL_ALREADY_EXISTS(432, "존재하는 이메일입니다."),
    INVALID_PASSWORD_FORMAT(433, "비밀번호 형식이 잘못되었습니다."),

    SERVER_ERROR(500, "서버 에러입니다. 서버 팀에 연락주세요!");


    private final int status;
    private final String message;
}
