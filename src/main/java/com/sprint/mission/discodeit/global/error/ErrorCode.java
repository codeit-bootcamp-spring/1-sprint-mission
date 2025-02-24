package com.sprint.mission.discodeit.global.error;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // common
    ENTITY_NOT_FOUND(400, "COMMON-001", " Entity Not Found"),
    INVALID_INPUT_VALUE(400, "COMMON-002", " Invalid Input Value"),

    // User
    USER_NOT_FOUND(404, "USER-001", "유저를 찾을 수 없습니다."),
    USER_ALREADY_EXIST(400, "USER-002", "이미 유저가 존재합니다."),
    USER_NOT_NULL(400, "USER-003", "유저는 null일 수 없습니다."),

    // Channel
    CHANNEL_NOT_FOUND(404, "CHANNEL-001", "채널을 찾을 수 없습니다."),
    NOT_CHANNEL_CREATOR(400, "CHANNEL-002", "채널 생성자가 아닙니다."),
    CHANNEL_OWNER_NOT_NULL(400, "CHANNEL-003","채널 소유자는 null일 수 없습니다."),
    CANNOT_UPDATE_PRIVATE_CHANNEL(400, "CHANNEL-004","PRIVATE 채널은 수정할 수 없습니다."),
    CHANNEL_NOT_NULL(400, "CHANNEL-005","채널은 null일 수 없습니다."),

    // Message
    MESSAGE_NOT_FOUND(404, "MESSAGE-001", "메세지 찾을 수 없습니다."),
    NOT_MESSAGE_CREATOR(400, "MESSAGE-002", "메세지 생성자가 아닙니다."),

    // File
    DIRECTORY_INIT_FAIL(500,"FILE-001", "디렉토리 생성에 실패했습니다."),
    FILE_WRITE_FAIL(500,"FILE-002", "파일 쓰기에 실패했습니다."),
    FILE_READ_FAIL(500,"FILE-003", "파일 읽기에 실패했습니다."),
    FILE_REMOVE_FAIL(500,"FILE-004", "파일 제거에 실패했습니다."),
    FILES_LOAD_FAIL(500,"FILE-005", "파일들을 로드하는데 실패했습니다."),
    FILE_CONVERSION_FAIL(500,"FILE-006", "파일을 바이트 배열로 변환하는 데 실패했습니다."),

    // ReadStatus
    READSTATUS_NOT_FOUND(404, "READSTATUS-001", "readStatus를 찾을 수 없습니다."),

    // BinaryContent
    BINARYCONTENT_NOT_NULL(400, "BINARYCONTENT-001", "BinaryContent는 null일 수 없습니다."),
    BINARYCONTENT_NOT_FOUND(404, "BINARYCONTENT-002", "binaryContent를 찾을 수 없습니다."),

    // UserStatus
    USERSTATUS_ALREADY_EXIST(400, "USERSTATUS-001", "이미 UserStatus가 존재합니다."),
    USERSTATUS_NOT_FOUND(404, "USERSTATUS-002", "userStatus를 찾을 수 없습니다.");

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(int status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public String format(Object... args) {
        StringBuilder sb = new StringBuilder(message);
        for (Object arg : args) {
            sb.append(" ").append(String.valueOf(arg));
        }
        return sb.toString();
    }
}
