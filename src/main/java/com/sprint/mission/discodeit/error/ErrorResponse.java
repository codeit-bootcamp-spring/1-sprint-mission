package com.sprint.mission.discodeit.error;


import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.Map;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@Schema(description = "Error Response Object")
@RequiredArgsConstructor
public class ErrorResponse {

  private final Instant timestamp;
  private final String code;
  private final String message;
  private final Map<String, Object> details;
  private final String exceptionType;
  private final int status;
}
