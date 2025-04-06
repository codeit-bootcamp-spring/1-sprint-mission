package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // User 관련 에러
    USER_NOT_FOUND("사용자를 찾을 수 없습니다."),
    DUPLICATE_USER("이미 존재하는 사용자입니다."),
    DUPLICATE_EMAIL("이미 존재하는 이메일입니다."),
    DUPLICATE_USERNAME("이미 존재하는 사용자명입니다."),
    INVALID_PASSWORD("잘못된 비밀번호입니다."),

    // Channel 관련 에러
    CHANNEL_NOT_FOUND("채널을 찾을 수 없습니다."),
    PRIVATE_CHANNEL_UPDATE("비공개 채널은 수정할 수 없습니다."),
    CHANNEL_ACCESS_DENIED("채널에 접근 권한이 없습니다."),

    // Message 관련 에러
    MESSAGE_NOT_FOUND("메시지를 찾을 수 없습니다."),
    MESSAGE_UPDATE_DENIED("메시지 수정 권한이 없습니다."),
    MESSAGE_DELETE_DENIED("메시지 삭제 권한이 없습니다."),

    // File 관련 에러
    FILE_NOT_FOUND("파일을 찾을 수 없습니다."),
    FILE_UPLOAD_FAILED("파일 업로드에 실패했습니다."),
    FILE_DOWNLOAD_FAILED("파일 다운로드에 실패했습니다."),
    FILE_DELETE_FAILED("파일 삭제에 실패했습니다."),
    INVALID_FILE_TYPE("지원하지 않는 파일 형식입니다."),
    FILE_SIZE_EXCEEDED("파일 크기가 제한을 초과했습니다."),

    // 공통 에러
    INVALID_INPUT("잘못된 입력값입니다."),
    INTERNAL_SERVER_ERROR("서버 내부 오류가 발생했습니다.");

    private final String message;
} 