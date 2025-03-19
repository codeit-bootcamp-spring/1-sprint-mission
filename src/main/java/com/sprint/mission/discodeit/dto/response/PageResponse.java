package com.sprint.mission.discodeit.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PageResponse<T> {

  private List<T> content;       // 조회된 데이터 목록
  private int size;              // 페이지 크기
  private boolean hasNext;       // 다음 페이지 존재 여부
  private String nextCursor;     // 다음 페이지 조회를 위한 커서 (예: 마지막 메시지의 ID 또는 createdAt)
}