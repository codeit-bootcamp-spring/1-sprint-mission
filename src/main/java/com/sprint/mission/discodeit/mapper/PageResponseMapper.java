package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PageResponseMapper<T> {

  public PageResponse<T> fromSlice(Slice<T> slice) {
    Object nextCursor = null;
    if (slice.hasContent()) {
      nextCursor = slice.getContent().get(slice.getContent().size() - 1);
    }
    return new PageResponse<>(slice.getContent(), nextCursor, slice.getSize(), slice.hasNext(),
        null);
  }

  public PageResponse<T> fromPage(Page<T> page) {
    Object nextCursor = null;
    if (page.hasContent()) {
      nextCursor = page.getContent().get(page.getContent().size() - 1);
    }
    return new PageResponse<>(page.getContent(), nextCursor, page.getSize(), page.hasNext(),
        page.getTotalElements());
  }
}
