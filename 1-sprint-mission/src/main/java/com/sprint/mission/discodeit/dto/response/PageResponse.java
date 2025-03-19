package com.sprint.mission.discodeit.dto.response;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PageResponse<T> {

  List<T> content;
  int number;
  int size;
  boolean hasNext;
  Long totalElements;

  public PageResponse(List<T> content, int number, int size, Long totalElements) {
    this.content = content;
    this.number = number;
    this.size = size;
    this.totalElements = totalElements;
  }

}
