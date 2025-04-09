package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // COMMON
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON000", "서버 에러, 관리자에게 연락해주세요."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "COMMON001", "잘못된 요청 입니다"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON002", "요청한 리소스를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "COMMON003", "허용되지 않은 HTTP 메서드입니다."),

    // User
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "VALID0001", "이메일 값이 유효하지 않습니다."),
    INVALID_USERNAME(HttpStatus.BAD_REQUEST, "VALID002", "USERNAME이 유효하지 않습니다."),
    INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "VALID003", "비밀번호가 유효하지 않습니다."),
    INVALID_PHONENUM(HttpStatus.BAD_REQUEST, "VALID004", "전화번호가 유효하지 않습니다."),
    INVALID_NICKNAME(HttpStatus.BAD_REQUEST, "VALID005", "별명이 유효하지 않습니다."),
    REGION_CODE_IS_NOT_NULL(HttpStatus.BAD_REQUEST, "VALID006", "Region Code는 Null이 될 수 없습니다."),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER001", "해당 사용자가 존재하지 않습니다."),
    USER_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "USER002", "해당 사용자 종류는 존재하지 않습니다."),
    USER_IS_ALREADY_EXIST(HttpStatus.CONFLICT, "USER003", "사용자가 이미 존재합니다."),
    USER_UPDATE_DATA_NOT_FOUND(HttpStatus.BAD_REQUEST, "USER004", "업데이트 할 데이터가 존재하지 않습니다."),

    // Channel
    OWNER_CANNOT_BLANK(HttpStatus.BAD_REQUEST, "CHANNEL001", "서버 주인은 NULL이 될 수 없습니다."),
    SERVERNAME_CANNOT_BLANK(HttpStatus.BAD_REQUEST, "CHANNEL002", "서버 이름은 NULL이 될 수 없습니다."),
    CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "CHANNEL003", "해당 채널이 존재하지 않습니다."),
    CHANNEL_TYPE_NOT_FOUND(HttpStatus.BAD_REQUEST, "CHANNEL004", "해당 채널 종류는 존재하지 않습니다."),
    PRIVATE_CANNOT_MODIFY(HttpStatus.FORBIDDEN, "CHANNEL005", "프라이빗 채널은 수정할 수 없습니다."),

    // Message
    AUTHOR_CANNOT_BLANK(HttpStatus.BAD_REQUEST, "MESSAGE001", "작성자는 NULL이 될 수 없습니다."),
    CHANNEL_CANNOT_BLANK(HttpStatus.BAD_REQUEST, "MESSAGE002", "작성 채널은 NULL이 될 수 없습니다."),
    CONTENT_CANANOT_BLANK(HttpStatus.BAD_REQUEST, "MESSAGE003", "작성 내용은 NULL이 될 수 없습니다."),
    MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "MESSAGE004", "해당 메시지가 존재하지 않습니다."),

    // Binary content
    BINARY_CONTENT_NOT_FOUND(HttpStatus.NOT_FOUND, "FILE001", "해당 파일이 존재하지 않습니다."),
    FILE_CONVERT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FILE002", "파일 변환 중 오류가 발생했습니다."),
    BINARY_CONTENT_SAVE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FILE003", "파일 메타데이터 저장에 실패하였습니다."),
    BINARY_CONTENT_LOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FILE004", "파일 메타데이터 조회에 실패하였습니다."),
    FILE_IS_EMPTY(HttpStatus.BAD_REQUEST, "FILE005", "빈 파일을 업로드할 수 없습니다."),

    // Storage
    FAILED_TO_CREATE_DIRECTORY(HttpStatus.INTERNAL_SERVER_ERROR, "STORAGE001", "디렉토리 생성에 실패하였습니다."),
    FAILED_TO_SAVE_LOCAL_STORAGE(HttpStatus.INTERNAL_SERVER_ERROR, "STORAGE002", "로컬 저장소 파일 저장을 실패하였습니다."),
    FAILED_TO_LOAD_LOCAL_STORAGE(HttpStatus.INTERNAL_SERVER_ERROR, "STORAGE003", "로컬 저장소 파일 로드를 실패하였습니다."),
    FILE_NOT_FOUND_IN_LOCAL_STORAGE(HttpStatus.NOT_FOUND, "STORAGE004", "해당 파일이 로컬 저장소에 존재하지 않습니다."),

    // User status
    USER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "USTAT001", "해당 USER STATUS가 존재하지 않습니다."),

    // Read status
    READ_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "RSTAT001", "해당 READ STATUS가 존재하지 않습니다."),

    // Auth
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "AUTH001", "로그인에 실패하였습니다."),

    // DATABASE (DB 처리 관련)
    FAILED_TO_SAVE_DATA(HttpStatus.INTERNAL_SERVER_ERROR, "DATABASE001", "데이터 저장에 실패하였습니다."),
    FAILED_TO_LOAD_DATA(HttpStatus.INTERNAL_SERVER_ERROR, "DATABASE002", "데이터 로딩에 실패하였습니다."),
    FAILED_TO_UPDATE_DATA(HttpStatus.INTERNAL_SERVER_ERROR, "DATABASE003", "데이터 갱신에 실패하였습니다."),
    FAILED_TO_DELETE_DATA(HttpStatus.INTERNAL_SERVER_ERROR, "DATABASE004", "데이터 삭제에 실패하였습니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;
}
