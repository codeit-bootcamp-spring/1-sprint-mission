package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ErrorResponse {

  private Instant timestamp;
  private String code;
  private String message;
  private Map<String, Object> details;
  private String exceptionType; // 발생한 예외의 클래스 이름
  private int status; //HTTP 상태코드

}
