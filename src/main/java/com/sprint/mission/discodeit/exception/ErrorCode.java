package com.sprint.mission.discodeit.exception;

public enum ErrorCode {
  USER_NOT_FOUND("User not found"),
  USER_EMAIL_ALREADY_EXISTS("User email already exists"),
  USER_USERNAME_ALREADY_EXISTS("User name already exists"),
  USERSTATUS_NOT_FOUND("UserStatus not found"),
  USERSTATUS_ALREADY_EXISTS("UserStatus already exists"),
  USER_WRONG_PASSWORD("Wrong password"),
  FILE_SAVE_ERROR("Failed to save file"),
  FILE_NOT_FOUND("File not found"),
  CHANNEL_NOT_FOUND("Channel not found"),
  PRIVATE_CHANNEL_UPDATE_NOT_ALLOWED("Private channel cannot be updated"),
  MESSAGE_NOT_FOUND("Message not found"),
  READ_STATUS_ALREADY_EXISTS("ReadStatus already exists"),
  READ_STATUS_NOT_FOUND("ReadStatus not found"),
  ;

  private final String message;

  ErrorCode(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }
}
