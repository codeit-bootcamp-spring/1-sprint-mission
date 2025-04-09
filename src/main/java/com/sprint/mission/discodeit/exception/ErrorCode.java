package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // auth
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 틀렸습니다."),
    // user
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 회원을 찾을 수 없습니다."),
    DUPLICATE_USER(HttpStatus.BAD_REQUEST,  "해당 회원이 이미 존재합니다."),
    // channel
    CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND,  "해당 채널을 찾을 수 없습니다."),
    PRIVATE_CHANNEL_UPDATE(HttpStatus.BAD_REQUEST,  "개인 채널은 수정할 수 없습니다."),
    // message
    MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 메세지를 찾을 수 없습니다."),
    // user status
    USERSTATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 회원 상태를 찾을 수 없습니다."),
    DUPLICATE_USERSTATUS(HttpStatus.BAD_REQUEST, "해당 회원 상태가 이미 존재합니다."),
    // read status
    READSTATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 읽음 상태를 찾을 수 없습니다."),
    DUPLICATE_READSTATUS(HttpStatus.BAD_REQUEST, "해당 읽음 상태가 이미 존재합니다."),
    // binary content
    BINARYCONTENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 파일이 존재하지 않습니다."),
    DIRECTORY_INIT_FAILED(HttpStatus.EXPECTATION_FAILED, "디렉토리 생성에 실패했습니다."),
    BINARYCONTENT_SAVE_FAILED(HttpStatus.EXPECTATION_FAILED, "파일 저장에 실패했습니다."),
    BINARYCONTENT_GET_FAILED(HttpStatus.EXPECTATION_FAILED, "파일을 얻지 못했습니다."),
    // global
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "담당자에게 문의해주세요.");

    private final HttpStatus httpStatus;
    private final String message;
}
