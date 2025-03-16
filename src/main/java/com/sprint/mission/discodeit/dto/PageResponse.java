package com.sprint.mission.discodeit.dto;


import java.util.List;
import lombok.Getter;

@Getter
public class PageResponse<T> {

  private final List<T> content;
  private final int number;
  private final int size;
  private final boolean hasNext;
  private final Long totalElements;

  public PageResponse(List<T> content, int number, int size, boolean hasNext, Long totalElements) {
    this.content = content;
    this.number = number;
    this.size = size;
    this.hasNext = hasNext;
    this.totalElements = totalElements;
  }
}