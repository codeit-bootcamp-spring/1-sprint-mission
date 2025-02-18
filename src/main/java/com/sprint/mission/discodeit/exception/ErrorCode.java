package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // COMMON
    INTERNAL_SERVER_ERROR("COMMON000", "서버 에러, 관리자에게 연락해주세요."),
    INVALID_INPUT_VALUE("COMMON001", "잘못된 값을 입력하였습니다"),

    // User
    INVALID_EMAIL("VALID0001", "이메일 값이 유효하지 않습니다."),
    INVALID_USERNAME("VALID002", "USERNAME이 유효하지 않습니다."),
    INVALID_PASSWORD("VALID003", "비밀번호가 유효하지 않습니다."),
    INVALID_PHONENUM("VALID004", "전화번호가 유효하지 않습니다."),
    INVALID_NICKNAME("VALID005", "별명이 유효하지 않습니다."),
    REGION_CODE_IS_NOT_NULL("VALID006", "Region Code는 Null이 될 수 없습니다."),

    USER_NOT_FOUND("USER001", "해당 사용자가 존재하지 않습니다."),
    USER_TYPE_NOT_FOUND("USER002", "해당 사용자 종류는 존재하지 않습니다."),
    USER_IS_ALREADY_EXIST("USER003", "사용자가 이미 존재합니다."),

    // Channel
    OWNER_CANNOT_BLANK("CHANNEL001", "서버 주인은 NULL이 될 수 없습니다."),
    SERVERNAME_CANNOT_BLANK("CHANNEL002", "서버 이름은 NULL이 될 수 없습니다."),
    CHANNEL_NOT_FOUND("CHANNEL003", "해당 채널이 존재하지 않습니다."),
    CHANNEL_TYPE_NOT_FOUND("CHANNEL004", "해당 채널 종류는 존재하지 않습니다."),
    PRIVATE_CANNOT_MODIFY("CHANNEL005", "프라이빗 채널은 수정할 수 없습니다."),

    // Message
    AUTHOR_CANNOT_BLANK("MESSAGE001", "작성자는 NULL이 될 수 없습니다."),
    CHANNEL_CANNOT_BLANK("MESSAGE002", "작성 채널은 NULL이 될 수 없습니다."),
    CONTENT_CANANOT_BLANK("MESSAGE003", "작성 내용은 NULL이 될 수 없습니다."),
    MESSAGE_NOT_FOUND("MESSAGE004", "해당 메시지가 존재하지 않습니다."),

    // DB
    FAILED_TO_CREATE_DIRECTORY("DATABASE001", "디렉토리 생성에 실패하였습니다."), // 얘는 진짜 DB 사용하면 삭제해도 될 듯 (FILE 떼문에 만들었음)
    FAILED_TO_SAVE_DATA("DATABASE002", "데이터 저장에 실패하였습니다."),
    FAILED_TO_LOAD_DATA("DATABASE003", "데이터 로딩에 실패하였습니다."),
    FAILED_TO_UPDATE_DATA("DATABASE004", "데이터 갱신에 실패하였습니다."),
    FAILED_TO_DELETE_DATA("DATABASE005", "데이터 삭제에 실패하였습니다."),

    // Binary content
    FILE_NOT_FOUND("BINARY001", "해당 파일이 존재하지 않습니다."),

    // User status
    USER_STATUS_NOT_FOUND("USTAT001", "해당 USER STATUS가 존재하지 않습니다."),

    // Read status
    READ_STATUS_NOT_FOUND("RSTAT001", "해당 READ STATUS가 존재하지 않습니다."),

    // Auth
    LOGIN_FAILED("AUTH001", "로그인에 실패하였습니다.");

    //    private final HttpStatus status; <- status code인데 springboot에 들어가 있는 것 같당
    private final String code;
    private final String message;

}
