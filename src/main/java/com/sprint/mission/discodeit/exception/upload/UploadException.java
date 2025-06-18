package com.sprint.mission.discodeit.exception.upload;

public abstract class UploadException extends RuntimeException {
  protected UploadException(String message, Throwable cause) {
    super(message, cause);
  }
}