package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.util.List;

public class PageResponseMapper {

  public <T> PageResponse<T> fromSlice(Slice<T> slice) {
    List<T> content = slice.getContent();
    int number = slice.getNumber();
    int size = slice.getSize();
    boolean hasNext = slice.hasNext();

    return new PageResponse<>(content, number, size, hasNext, null);
  }

  public <T> PageResponse<T> fromPage(Page<T> page) {
    List<T> content = page.getContent();
    int number = page.getNumber();
    int size = page.getSize();
    boolean hasNext = page.hasNext();
    Long totalElements = page.getTotalElements();

    return new PageResponse<>(content, number, size, hasNext, null);
  }
}