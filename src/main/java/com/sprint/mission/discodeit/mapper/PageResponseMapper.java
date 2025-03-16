package com.sprint.mission.discodeit.mapper;


import com.sprint.mission.discodeit.dto.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

public class PageResponseMapper {

  public static <T> PageResponse<T> fromSlice(Slice<T> slice) {
    return new PageResponse<>(
        slice.getContent(),
        slice.getNumber(),
        slice.getSize(),
        slice.hasNext(),
        null  // Slice는 전체 개수를 제공하지 않음
    );
  }

  public static <T> PageResponse<T> fromPage(Page<T> page) {
    return new PageResponse<>(
        page.getContent(),
        page.getNumber(),
        page.getSize(),
        page.hasNext(),
        page.getTotalElements()
    );
  }
}