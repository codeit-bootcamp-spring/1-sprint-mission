package com.spirnt.mission.discodeit.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    USER_NOT_FOUND("USER_404", HttpStatus.NOT_FOUND, "User를 찾을 수 없습니다."),
    DUPLICATE_USER("USER_400_1", HttpStatus.BAD_REQUEST, "이메일 또는 이름이 중복됩니다."),
    INVALID_PASSWORD("USER_400_2", HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    CHANNEL_NOT_FOUND("CHANNEL_404", HttpStatus.NOT_FOUND, "Channel을 찾을 수 없습니다."),
    PRIVATE_CHANNEL_UPDATE("CHANNEL_400", HttpStatus.BAD_REQUEST, "비공개 채널은 수정할 수 없습니다."),

    MESSAGE_NOT_FOUND("MESSAGE_404", HttpStatus.NOT_FOUND, "Message를 찾을 수 없습니다."),

    READSTATUS_NOT_FOUND("READSTATUS_404", HttpStatus.NOT_FOUND, "ReadStatus를 찾을 수 없습니다."),
    READSTATUS_ALREADY_EXISTS("READSTATU_400", HttpStatus.BAD_REQUEST, "이미 동일한 ReadStatus가 존재합니다."),

    BINARYCONTENT_NOT_FOUND("BINARYCONTENT_404", HttpStatus.NOT_FOUND, "BinaryContent를 찾을 수 없습니다."),
    FILE_ERROR("BINARYCONTENT_500", HttpStatus.INTERNAL_SERVER_ERROR, "파일을 읽거나 쓰는 중 오류가 발생했습니다."),

    JWT_SESSION_NOT_FOUND("AUTH_401_1", HttpStatus.UNAUTHORIZED, "Jwt 인증 정보가 없습니다."),
    INVALID_JWT_TOKEN("AUTH_401_2", HttpStatus.UNAUTHORIZED, "JWT 토큰이 유효하지 않습니다."),
    INVALID_TOKEN_SECRET("AUTH", HttpStatus.INTERNAL_SERVER_ERROR, "JWT 토큰 시크릿 키가 유효하지 않습니다. "),

    NOTIFICATION_NOT_FOUND("NOTIFICATION_404", HttpStatus.NOT_FOUND, "Notification을 찾을 수 없습니다.");

    private final String code;
    private final HttpStatus status;
    private final String message;

    ErrorCode(String code, HttpStatus status, String message) {
        this.code = code;
        this.status = status;
        this.message = message;
    }


}
