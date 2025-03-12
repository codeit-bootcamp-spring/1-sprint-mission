package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public class PageResponseMapper {

  public <T> PageResponse<T> fromSlice(Slice<T> slice) {
    return PageResponse.<T>builder()
        .contents(slice.getContent())
        .number(slice.getNumber())
        .size(slice.getSize())
        .hasNext(slice.hasNext())
        .totalElements(null) // Slice는 전체 데이터 수를 계산하지 않는다.
        .build();
  }

  public <T> PageResponse<T> fromPage(Page<T> page) {
    return PageResponse.<T>builder()
        .contents(page.getContent())
        .number(page.getNumber())
        .size(page.getSize())
        .hasNext(page.hasNext())
        .totalElements(page.getTotalElements())
        .build();
  }
}
