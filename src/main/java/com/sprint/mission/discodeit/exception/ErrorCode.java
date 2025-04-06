package com.sprint.mission.discodeit.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

	USER_NOT_FOUND("존재하지 않는 유저입니다"),
	DUPLICATE_USER("이미 존재하는 유저입니다"),
	CHANNEL_NOT_FOUND("존재하지 않는 채널입니다"),
	PRIVATE_CHANNEL_UPDATE("비공개 채널은 수정할 수 없습니다"),
	;

	ErrorCode(String message) {
		this.message = message;
	}

	private final String message;
}
