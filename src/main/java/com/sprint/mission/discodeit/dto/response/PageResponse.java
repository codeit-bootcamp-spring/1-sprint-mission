package com.sprint.mission.discodeit.dto.response;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponse<T> {

  private List<T> content;
  private String nextCursor;
  private int size;
  private Long totalElements;
  private boolean hasNext;
}
