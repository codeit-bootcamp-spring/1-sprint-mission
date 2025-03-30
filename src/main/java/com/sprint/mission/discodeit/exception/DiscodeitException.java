package com.sprint.mission.discodeit.exception;

import java.time.Instant;
import java.util.Map;
import lombok.Getter;

@Getter
public class DiscodeitException extends RuntimeException {

  private final Instant timestamp;
  private final ErrorCode errorCode;
  /**
   * 예외 발생 상황에 대한 추가정보를 저장하기 위한 속성 e.g. 조회 시도한 사용자의 ID 정보 e.g. 업데이트 시도한 PRIVATE 채널의 ID 정보
   **/
  private final Map<String, Object> details;

  public DiscodeitException(Instant timestamp, ErrorCode errorCode, Map<String, Object> details) {
    super(errorCode.getMessage()); // 부모 클래스인 RuntimeException의 생성자 호출, 에외 메세지를 설정한다.
    this.timestamp = timestamp != null ? timestamp : Instant.now();
    this.errorCode = errorCode;
    this.details = details;
  }
}
