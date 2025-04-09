package com.sprint.mission.discodeit.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON-500", "Internal server error"),
	INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "COMMON-400", "Invalid input value"),

	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-404", "User not found"),
	USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER-409", "User already exists"),
	USER_EMAIL_DUPLICATED(HttpStatus.CONFLICT, "USER-409-EMAIL", "User email already exists"),
	USER_USERNAME_DUPLICATED(HttpStatus.CONFLICT, "USER-409-USERNAME", "User username already exists"),

	CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "CHANNEL-404", "Channel not found"),
	CHANNEL_PRIVATE_UPDATE_DENIED(HttpStatus.FORBIDDEN, "CHANNEL-403", "Cannot update private channel"),

	MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "MESSAGE-404", "Message not found"),

	FILE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FILE-500", "File upload failed"),
	FILE_NOT_FOUND(HttpStatus.INTERNAL_SERVER_ERROR, "FILE-500", "File not found"),
	FILE_DOWNLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "FILE-500-DOWNLOAD", "File download failed"),

	USER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "UserStatus-400", "UserStatus Not Found"),

	WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "AUTH-400", "Password failed");

	private final HttpStatus httpStatus;
	private final String code;
	private final String message;
}