package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public class PageResponseMapper {


  public <T> PageResponse<T> fromSlice(Slice<T> slice) {
    return new PageResponse<>(
        slice.getContent(),
        slice.getNumber(),
        slice.getSize(),
        slice.hasNext(),
        null  // Slice는 총 요소 수를 알 수 없음
    );
  }


  public static <T> PageResponse<T> fromPage(Page<T> page, String nextCursor) {
    return new PageResponse<>(
        page.getContent(),
        nextCursor,
        page.getSize(),
        page.hasNext(),
        page.getTotalElements()
    );
  }
}