package com.sprint.mission.discodeit.dto.response;

import java.time.Instant;
import java.util.Map;


public record ErrorResponse(
    Instant timestamp,
    String code,
    String message,
    Map<String, Object> details,
    String exceptionType, //발생한 예외 클래스 이름
    int status //HTTP 상태코드
) {


}
