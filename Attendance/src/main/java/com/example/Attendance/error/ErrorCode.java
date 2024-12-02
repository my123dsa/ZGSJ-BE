package com.example.Attendance.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    INVALID_PARAMETER(400, "파라미터 값을 확인해주세요."),
    WRONG_BODY(400, "REQUESTBODY에서 잘못된 값이 있습니다."),

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
    CANNOT_FIND_POSITION(400, "위치가 잘못됨"),
    INVALID_EMPLOYEE(400, "직원 정보가 없음"),
    INVALID_LOCATION(403, "위치가 잘못됨"),
    INVALID_COMMUTE(400, "통근 정보가 없음"),
    MISSING_GO_TO_WORK_RECODE(400, "출근 찍지 않음"),
    MISSING_LEAVE_WORK_RECODE(400, "퇴근 찍지 않음"),
    API_SERVER_ERROR(400, "금융 서버 오류 "),
    INVALID_ENCRYPTION(401, "암호화가 유효하지 않습니다."),
    INVALID_DECRYPTION(401, "복호화가 유효하지 않습니다."),
    INVALID_PAY_STATEMENT(400, "급여 기록 없음"),
    INSUFFICIENT_BALANCE(400, "출금 금액이 잔액보다 큽니다."),
    ACCOUNT_NOT_FOUND(404, "계좌 정보를 찾을 수 없습니다."),
    ERROR_NOT_FOUND(400, "FEGIN에서 에러 정보를 찾을 수 없습니다."),
    RETRY_BATCH_STEP(503, "일시적인 서버 오류가 발생했습니다. 재시도 중입니다."),
    EMAIL_SEND_FAILED(500, "이메일 전송에 실패했습니다."),
    PDF_CREATE_ERROR(500, "PDF 생성 중 오류가 발생했습니다."),
    GCP_SETTING_ERROR(500, "구글 클라우드 설정 중 오류가 발생했습니다."),
    GCP_ERROR(500, "구글 클라우드에 저장 중 오류가 발생했습니다."),
    Feign_Error(500,"MSA 통신 에러"),
    UPDATE_FAIL(500,"type 업데이트 에러");

    private final int status;
    private final String message;
}
