package com.spirnt.mission.discodeit.dto.response;

import java.util.List;
import lombok.Getter;

@Getter
public class PageResponse<T> {

  private List<T> content;  // 실제 데이터
  private Object nextCursor;
  private int size; // 페이지 크기
  private boolean hasNext;
  private Long totalElements; // T 데이터의 총 갯수

  public PageResponse(List<T> content, Object nextCursor, int size,
      boolean hasNext, long totalElements) {
    this.content = content;
    this.nextCursor = nextCursor;
    this.size = size;
    this.hasNext = hasNext;
    this.totalElements = totalElements;
  }

}
