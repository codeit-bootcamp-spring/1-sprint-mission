package com.sprint.mission.discodeit.execption;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

  private Instant timestamp;
  private String errorCode;
  private String errorMessage;
  private Map<String, Object> details = new HashMap<>();
  private String exceptionType;
  private int status;
}
