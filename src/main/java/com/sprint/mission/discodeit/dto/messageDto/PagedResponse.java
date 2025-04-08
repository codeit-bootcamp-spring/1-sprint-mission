package com.sprint.mission.discodeit.dto.messageDto;

import java.util.List;
import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

@Getter
public class PagedResponse<T> {

  private final List<T> content;
  private final int number;
  private final int size;
  private final Long totalElements; // Optional (null 가능)
  private final boolean hasNext;

  public PagedResponse(List<T> content, int number, int size, Long totalElements, boolean hasNext) {
    this.content = content;
    this.number = number;
    this.size = size;
    this.totalElements = totalElements;
    this.hasNext = hasNext;
  }

  public static <T> PagedResponse<T> fromSlice(Slice<T> slice) {
    return new PagedResponse<>(
        slice.getContent(),
        slice.getNumber(),
        slice.getSize(),
        null,
        slice.hasNext()
    );
  }

  public static <T> PagedResponse<T> fromPage(Page<T> page) {
    return new PagedResponse<>(
        page.getContent(),
        page.getNumber(),
        page.getSize(),
        page.getTotalElements(),
        page.hasNext()
    );
  }
}