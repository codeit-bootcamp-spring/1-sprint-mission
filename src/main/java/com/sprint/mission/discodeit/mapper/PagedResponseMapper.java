package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.messageDto.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public class PagedResponseMapper {

  public <T> PagedResponse<T> toPagedResponse(Slice<T> slice) {
    return PagedResponse.fromSlice(slice);
  }

  public <T> PagedResponse<T> toPagedResponse(Page<T> page) {
    return PagedResponse.fromPage(page);
  }
}
