package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	//Common
	INVALID_INPUT_VALUE(400, "C001", "Invalid input value"),
	RESOURCE_NOT_FOUND(404, "C002", "Resource not found"),
	INTERNAL_SERVER_ERROR(500, "C003", "Internal server error"),

	//User
	USER_NOT_FOUND(404, "U001", "User not found"),
	USER_ALREADY_EXISTS(400, "U002", "User already exists"),

	//Channel
	CHANNEL_NOT_FOUND(404, "CH001", "Channel not found"),
	PRIVATE_CHANNEL_UPDATE(403, "CH002", "Private channel cannot be updated"),

	//Message
	MESSAGE_NOT_FOUND(404, "M001", "Message not found"),

	//File
	FILE_UPLOAD_ERROR(500, "F001", "File upload error"),
	FILE_DOWNLOAD_ERROR(500, "F002", "File download error");

	private final int status;
	private final String code;
	private final String message;
}
