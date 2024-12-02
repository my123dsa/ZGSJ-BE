package com.example.Attendance.error.log;

import lombok.Getter;

@Getter
public enum ErrorType {
    FEIGN_EXCEPTION("금융서버와의 통신 중 오류가 발생했습니다."),
    INTERNAL_ERROR("내부 시스템 처리 중 오류가 발생했습니다.");

    private final String message;

    ErrorType(String message) {
        this.message = message;
    }
}