package com.sprint.mission.discodeit.dto.response;

import java.util.List;

public class PageResponse<T> {
  private List<T> content;
  private int number;
  private int size;
  private boolean hasNext;
  private long totalElements;

  public PageResponse(List<T> content, int number, int size, boolean hasNext, long totalElements) {
    this.content = content;
    this.number = number;
    this.size = size;
    this.hasNext = hasNext;
    this.totalElements = totalElements;
  }
}
