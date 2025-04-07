package com.sprint.mission.discodeit.dto.error;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(
    Instant timestamp,
    String code,
    String message,
    Map<String, Object> detail,
    String exceptionType,   // 발생한 예외의 클래스 이름
    int status    // HTTP 상태 코드
) {

}
