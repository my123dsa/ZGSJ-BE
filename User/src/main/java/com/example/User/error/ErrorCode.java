package com.example.User.error;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    INVALID_PARAMETER(400, "파라미터 값을 확인해주세요."),
    NO_STORE(404, "가게가 존재하지 않습니다"),
    WRONG_VALID(400, "유효성 검사와 어긋나는 요청입니다"),

    CHECKUP_NOT_FOUND(404, "존재하지 않는 이해도조사 ID 입니다."),
    ROOM_NOT_FOUND(404, "존재하지 않는 방 ID 입니다."),
    ROOM_UUID_NOT_FOUND(404, "존재하지 않는 uuid 입니다."),
    COMMENT_NOT_FOUND(404, "존재하지 않는 메세지 ID 입니다."),
    MEMBER_NOT_FOUND(404, "존재하지 않는 사용자 ID 입니다."),
    USER_NOT_FOUND_BY_NAME_AND_PHONE(404,"일치하는 사용자 정보를 찾을 수 없습니다"),
    SNAPSHOT_NOT_FOUND(404, "존재하지 않는 스냅샷 ID 입니다."),
    PRESIDENT_NOT_FOUND(404, "사장님을 찾을 수 없습니다."),
    STORE_NOT_FOUND(404, "가게를 찾을 수 없습니다."),
    ACCOUNT_NOT_FOUND(404,"계좌를 찾을 수 없습니다."),

    DUPLICATE_STORE_NAME(409, "이미 존재하는 가게 이름입니다."),

    INVALID_EMAIL_FORMAT(431, "이메일 형식 잘못되었습니다."),
    INVALID_PASSWORD_FORMAT(433, "비밀번호 형식이 잘못되었습니다."),

    SERVER_ERROR(500, "서버 에러입니다. 서버 팀에 연락주세요!"),
    DB_DUPLICAE_ERROR(500, "DB 중복 ERROR입니다. DB팀에 연락해주세요"),
    EMAIL_SEND_FAILED(500, "이메일 전송에 실패했습니다."),
    MISMATCH_EMAIL(503, "이메일과 이름이 일치하지 않습니다."),

    USER_NOT_FOUND(404, "해당 아이디에 일치하는 유저가 없습니다"),
    STOREEMPLOYEE_NOT_FOUND(404, "해당 아이디에 일치하는 유저가 없습니다"),
    PASSWORD_NOT_CORRECT(400, "비밀번호가 다릅니다"),
    BUSINESSNUMBER_NOT_FOUND(404, "해당 사업자 번호가 없습니다"),
    EMAIL_ALREADY_EXISTS(409 , "존재하는 이메일입니다."),
    EMPTY_REFRESH_TOKEN(400 , "리프레시 토큰이 없습니다."),
    INVALID_REFRESH_TOKEN(401,"리프레시 토큰이 유효하지 않습니다."),
    INVALID_ENCRYPTION(401,"암호화가 유효하지 않습니다."),
    INVALID_DECRYPTION(401,"복호화가 유효하지 않습니다."),
    CANNOT_FIND_POSITION(400,"위치가 잘못됨"),
    INVALID_REQUEST(400, "잘못된 요청입니다"),
    INVALID_NUMBER_FORMAT(400, "숫자 형식이 올바르지 않습니다"),
    INVALID_EMPLOYEE(400,"직원 정보가 없음"),
    PASSWORD_CONFIRM_NOT_MATCH(400,"새 비밀번호와 확인 비밀번호가 일치하지 않습니다."),
    BANK_ERROR(404,"은행쪽 에러입니다.");


    private final int status;
    private final String message;
}
