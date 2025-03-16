package com.sprint.mission.discodeit.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PageResponse<T> {

  List<T> content;
  int number;
  int size;
  boolean hasNext;
  Long totalElements;

}
