package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Slice;

@Mapper(componentModel = "spring")
public interface PageResponseMapper {

  default <T> PageResponse<T> fromSlice(Slice<T> slice, Object nextCursor) {
    return new PageResponse<>(
        slice.getContent(),
        (String) nextCursor,
        slice.getSize(),
        null,
        slice.hasNext()
    );
  }


  default <T> PageResponse<T> fromPage(
      List<T> contents,
      boolean hasNext,
      int size,
      Object nextCursor,
      Long totalElements) {

    // contents가 null이면 빈 리스트로 대체
    List<T> safeContents = contents != null ? contents : new ArrayList<>();

    return PageResponse.<T>builder()
        .content(safeContents)
        .hasNext(hasNext)
        .size(size)
        .nextCursor((String) nextCursor)
        .totalElements(totalElements)
        .build();
  }
}
