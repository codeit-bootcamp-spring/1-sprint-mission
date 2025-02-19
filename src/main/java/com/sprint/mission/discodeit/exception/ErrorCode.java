package com.sprint.mission.discodeit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    EMPTY_CONTENT("내용을 입력해주세요"),

    CANNOT_FOUND_MESSAGE("보낸 메시지가 없습니다"),
    CANNOT_FOUND_CHANNEL("해당 채널을 찾을 수가 없습니다."),
    CANNOT_FOUND_USER("해당하는 회원을 찾을 수 없습니다"),
    CANNOT_FOUND_PROFILE("해당하는 프로필 데이터를 찾을 수 없습니다"),
    CANNOT_FOUND_USERSTATUS("해당하는 사용자의 상태 정보를 찾을 수 없습니다"),
    CANNOT_FOUND_READSTATUS("해당하는 Read Status 정보를 찾을 수 없습니다"),
    CANNOT_FOUND_ATTACHMENT("해당하는 첨부자료를 찾을 수 없습니다"),

    DUPLICATE_NAME("이미 등록된 사용자 이름입니다."),
    DUPLICATE_PHONE("이미 등록된 핸드폰 번호 입니다."),
    DUPLICATE_CHANNEL("이미 존재하는 채널 이름 입니다."),

    ALREADY_EXIST_READSTATUS("이미 저장된 Read Status 입니다."),
    ALREADY_EXIST_USERSTAUTS("이미 저장된 User Status 입니다."),

    INVALID_PASSWORD("올바르지 않은 비밀번호 형식 입니다. 비밀번호는 8자리 이상 15자리 이하 대문자 및 특수문자 하나 이상 포함해야 합니다."),
    INVALID_WRITER("작성자가 올바르지 않습니다"),
    INVALID_PHONE("올바르지 않은 핸드폰 번호 형식 입니다."),

    MESSAGE_EDIT_NOT_ALLOWED("메시지 수정은 작성자 본인만 가능합니다"),

    PASSWORD_MISMATCH("비밀번호가 일치하지 않습니다"),
    USERNAME_MISMATCH("이름이 일치하지 않습니다"),
    CHANNEL_TYPE_MISMATCH("채널 타입이 일치하지 않습니다");


    private final String description;
    }
