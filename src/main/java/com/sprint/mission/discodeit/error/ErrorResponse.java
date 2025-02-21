package com.sprint.mission.discodeit.error;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@Schema(description = "Error Response Object")
@RequiredArgsConstructor
public class ErrorResponse {
  private final ErrorDetail error;
}
