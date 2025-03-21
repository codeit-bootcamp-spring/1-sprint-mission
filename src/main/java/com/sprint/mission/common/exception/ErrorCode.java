package com.sprint.mission.common.exception;

import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
@AllArgsConstructor
public enum ErrorCode {
    NOT_VALID_ARGUMENT(BAD_REQUEST, "바디의 파라미터가 적절하지 못합니다."),

    NO_SUCH_USER(NOT_FOUND, "존재하지 않는 유저입니다"),
    NO_SUCH_CHANNEL(NOT_FOUND, "존재하지 않는 채널입니다"),
    NO_SUCH_MESSAGE(NOT_FOUND, "존재하지 않는 메시지입니다"),
    NO_SUCH_BINARY(NOT_FOUND, "존재하지 않는 Binary_Content 입니다"),
    NO_SUCH_USER_STATUS(NOT_FOUND, "존재하지 않는 유저 상태입니다"),
    NO_SUCH_READ_STATUS(NOT_FOUND, "존재하지 않는 READ 상태입니다"),
    NO_SUCH_USER_MATCHING_NAME(NOT_FOUND, "존재하지 않는 유저 이름입니다"),
    NO_SUCH_STATUS_MATCHING_USER(NOT_FOUND, "유저 id에 맞는 상태 정보가 존재하지 않습니다"),
    NO_SUCH_PROFILE_MATCHING_USER(NOT_FOUND, "유저 id에 맞는 프로필정보가 존재하지 않습니다"),
    NO_SUCH_BINARY_MATCHING_MESSAGE(NOT_FOUND, "메세지 id에 맞는 Binary 데이터가 존재하지 않습니다"),
    NO_SUCH_READ_STATUS_MATCHING_CHANNEL(NOT_FOUND, "채널 id에 맞는 READ_STATUS가 존재하지 않습니다"),

    INCORRECT_PASSWORD(BAD_REQUEST, "비밀번호가 일치하지 않습니다"),

    CANNOT_REQUEST_LAST_READ_TIME(FORBIDDEN, "Public 채널은 유저별 lastReadTime 호출 불가"),
    CANNOT_UPDATE_PRIVATE_CHANNEL(FORBIDDEN, "Private 채널은 수정 불가능입니다."),

    ALREADY_EXIST_USER_STATUS(CONFLICT, "이미 존재하는 유저 상태입니다"),
    ALREADY_EXIST_READ_STATUS(CONFLICT, "이미 존재하는 READ 상태입니다"),
    ALREADY_EXIST_NAME(CONFLICT, "이미 존재하는 이름입니다"),
    ALREADY_EXIST_EMAIL(CONFLICT, "이미 존재하는 이메일입니다"),

    FILE_CONVERT_ERROR(INTERNAL_SERVER_ERROR, "파일 변환 중 오류가 발생했습니다."),
    FILE_CREATE_ERROR(INTERNAL_SERVER_ERROR, "파일 생성 중 오류가 발생했습니다."),
    DIRECTORY_CREATE_ERROR(INTERNAL_SERVER_ERROR, "폴더 생성 중 오류가 발생했습니다."),
    FAIL_TO_INIT_DIRECTORY(INTERNAL_SERVER_ERROR, "디렉토리 초기화 실패했습니다"),
    FILE_WRITE_ERROR(INTERNAL_SERVER_ERROR, "파일 객체 생성 중 오류가 발생했습니다."),
    NO_SUCH_USER_FILE(NOT_FOUND, "USER 파일 객체가 존재하지 않습니다."),
    NO_SUCH_CHANNEL_FILE(NOT_FOUND, "USER 파일 객체가 존재하지 않습니다."),
    NO_SUCH_MESSAGE_FILE(NOT_FOUND, "메시지 파일 객체가 존재하지 않습니다."),
    NO_SUCH_BINARY_FILE(NOT_FOUND, "Binary 파일 객체가 존재하지 않습니다."),
    NO_SUCH_READ_STATUS_FILE(NOT_FOUND, "ReadStatus 파일 객체가 존재하지 않습니다."),
    NO_SUCH_USER_STATUS_FILE(NOT_FOUND, "UserStatus 파일 객체가 존재하지 않습니다."),
    FILE_DELETE_ERROR(BAD_REQUEST, "파일 삭제 실패했습니다"),;
    private final HttpStatus status;
    private final String message;
}
