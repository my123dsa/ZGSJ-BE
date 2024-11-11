package com.example.Attendance.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    INVALID_PARAMETER(400, "파라미터 값을 확인해주세요."),

    CHECKUP_NOT_FOUND(404, "존재하지 않는 이해도조사 ID 입니다."),
    ROOM_NOT_FOUND(404, "존재하지 않는 방 ID 입니다."),
    ROOM_UUID_NOT_FOUND(404, "존재하지 않는 uuid 입니다."),
    COMMENT_NOT_FOUND(404, "존재하지 않는 메세지 ID 입니다."),
    MEMBER_NOT_FOUND(404, "존재하지 않는 사용자 ID 입니다."),
    SNAPSHOT_NOT_FOUND(404, "존재하지 않는 스냅샷 ID 입니다."),

    INVALID_EMAIL_FORMAT(431, "이메일 형식 잘못되었습니다."),
    EMAIL_ALREADY_EXISTS(432, "존재하는 이메일입니다."),
    INVALID_PASSWORD_FORMAT(433, "비밀번호 형식이 잘못되었습니다."),

    SERVER_ERROR(500, "서버 에러입니다. 서버 팀에 연락주세요!"),
    CANNOT_FIND_POSITION(400,"위치가 잘못됨"),
    INVALID_EMPLOYEE(400,"직원 정보가 없음"),
    INVALID_LOCATION(400,"직원 정보가 없음"),
    INVALID_COMMUTE(400,"통근 정보가 없음"),
    MISSING_GO_TO_WORK_RECODE(400,"출근 찍지 않음"),
    MISSING_LEAVE_WORK_RECODE(400,"퇴근 찍지 않음");


    private final int status;
    private final String message;
}
