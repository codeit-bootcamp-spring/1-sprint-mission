package com.sprint.mission.discodeit.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {
  NEW_MESSAGE("추가 메시지", "메시지가 등록되었습니다. 확인해보세요."),
  ROLE_CHANGED("권한 변경", "권한이 변경되었습니다."),
  ASYNC_FAILED("작업 실패", "비동기 작업이 실패하였습니다.");

  private final String title;
  private final String message;

}
