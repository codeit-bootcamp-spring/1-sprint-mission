package com.sprint.mission.discodeit.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PageResponse<T> {

  List<T> content;
  Object nextCursor;
  int size;
  boolean hasNext;
  Long totalElements;

}
