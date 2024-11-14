package com.example.Attendance.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    INVALID_PARAMETER(400, "파라미터 값을 확인해주세요."),
    SERVER_ERROR(500, "서버 에러입니다. 서버 팀에 연락주세요!"),
    CANNOT_FIND_POSITION(400,"위치가 잘못됨"),
    INVALID_EMPLOYEE(400,"직원 정보가 없음"),
    INVALID_LOCATION(403,"위치가 잘못됨"),
    INVALID_COMMUTE(400,"통근 정보가 없음"),
    MISSING_GO_TO_WORK_RECODE(400,"출근 찍지 않음"),
    MISSING_LEAVE_WORK_RECODE(400,"퇴근 찍지 않음");


    private final int status;
    private final String message;
}
