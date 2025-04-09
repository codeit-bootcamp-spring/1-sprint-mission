package com.sprint.mission.discodeit.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

  UNHANDLED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SERVER-001", "Unknown Error."),
  DEFAULT_ERROR_MESSAGE(HttpStatus.BAD_REQUEST, "GENERAL-001", "Unauthorized Operation."),
  INVALID_UUID_FORMAT(HttpStatus.BAD_REQUEST, "GENERAL-002", "Invalid UUID Format."),
  VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "VALIDATION-001", "Invalid Input Format."),

  CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "CHANNEL-001", "Could Not Find Channel"),
  NO_ACCESS_TO_CHANNEL(HttpStatus.UNAUTHORIZED, "CHANNEL-002", "Unauthorized Access to Channel."),
  PRIVATE_CHANNEL_CANNOT_BE_UPDATED(HttpStatus.FORBIDDEN, "CHANNEL-003",
      "Private Channel Cannot Be Modified"),

  USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-001", "Could Not Find User"),
  PASSWORD_MATCH_ERROR(HttpStatus.UNAUTHORIZED, "USER-002", "Invalid Password."),

  FILE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "FILE-001", "File Error"),
  ERROR_WHILE_DOWNLOADING(HttpStatus.INTERNAL_SERVER_ERROR, "FILE-002", "Failed To Download File"),
  IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "FILE-003", "Could Not Find Image."),

  MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "MESSAGE-001", "Could Not Find Message."),

  READ_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "READ-STATUS-001", "Could Not Find ReadStatus."),

  USER_STATUS_NOT_FOUND(HttpStatus.NOT_FOUND, "USER-STATUS-001", "Could Not Find UserStatus.");


  private final HttpStatus status;
  private final String code;
  private final String message;
}
