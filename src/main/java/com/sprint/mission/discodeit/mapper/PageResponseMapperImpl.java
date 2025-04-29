package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public class PageResponseMapperImpl implements PageResponseMapper {

  @Override
  public <T> PageResponse<T> fromSlice(Slice<T> slice, Object nextCursor) {
    return new PageResponse<>(
        slice.getContent(),
        nextCursor,
        slice.getSize(),
        slice.hasNext(),
        null
    );
  }

  @Override
  public <T> PageResponse<T> fromPage(Page<T> page, Object nextCursor) {
    return new PageResponse<>(
        page.getContent(),
        nextCursor,
        page.getSize(),
        page.hasNext(),
        page.getTotalElements()
    );
  }
}
