package com.sprint.mission.discodeit.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class PageResponse<T> {

  private List<T> content;
  private int number;
  private int size;
  private boolean hasNext;
  private Long totalElements;
}