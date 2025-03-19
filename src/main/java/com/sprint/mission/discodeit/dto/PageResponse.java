package com.sprint.mission.discodeit.dto;


import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class PageResponse<T> {

  private final List<T> content;
  //private final int number;
  private Instant nextCursor;
  private final int size;
  private final boolean hasNext;
  private final Long totalElements;

}