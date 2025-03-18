package com.sprint.mission.discodeit.dto.response;

import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageResponse<T> {

  private List<T> content;
  private Object nextCursor;
  private int size;
  private boolean hasNext;
  private Long totalElements;

  public PageResponse(List<T> content, Object nextCursor, int size, boolean hasNext,
      Long totalElements) {
    this.content = content;
    this.nextCursor = nextCursor;
    this.size = size;
    this.hasNext = hasNext;
    this.totalElements = totalElements;
  }
}
