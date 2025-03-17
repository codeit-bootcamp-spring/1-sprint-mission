package com.sprint.mission.discodeit.dto.response;

import java.util.List;

public record PageResponse <T>(
  List<T> content,
  Object nextCursor,
  int number,
  int size,
  boolean hasNext,
  Long totalElement
){

}
