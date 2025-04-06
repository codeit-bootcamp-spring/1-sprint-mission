package com.sprint.mission.discodeit.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  USER_NOT_FOUND(404, "User not found"),
  CHANNEL_NOT_FOUND(404, "Channel not found"),
  DUPLICATE_USER(404, "User is duplicated"),
  PRIVATE_CHANNEL_UPDATE(404, "Private channel is updated");

  private final int status;
  private final String message;
}