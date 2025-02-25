package com.sprint.mission.discodeit.error;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Schema(description = "Description on Error")
@RequiredArgsConstructor
public class ErrorDetail {

  @Schema(description = "HTTP Status Code", example = "4xx")
  private final int code;

  @Schema(description = "Error Message", example = "Error Response")
  private final String message;
}
