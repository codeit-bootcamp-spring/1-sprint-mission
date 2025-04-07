package com.sprint.mission.discodeit.error;

import java.time.Instant;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class ErrorResponse {
    Instant timestamp;
    String code;
    String message;
    Map<String, Object> details;
    String exceptionType;
    int status;
}
