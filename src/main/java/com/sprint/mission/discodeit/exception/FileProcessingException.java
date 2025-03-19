package com.sprint.mission.discodeit.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileProcessingException extends RuntimeException {

  private final ErrorCode errorCode;
}