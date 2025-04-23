package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.page.PageResponse;
import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

@Mapper(componentModel = "spring")
public interface PageResponseMapper {

  // 커서 기반 페이지네이션
  default <T> PageResponse<T> fromSlice(Slice<T> slice, Object nextCursor) {
    return new PageResponse<>(
        slice.getContent(),   // 현재 페이지의 데이터 리스트
        nextCursor,           // 다음 페이지를 가져올 커서
        slice.getSize(),      // 한 페이지의 크기
        slice.hasNext(),      // 다음 페이지가 있는지 여부
        null                  // Slice 객체 총 개수 제공 X
    );
  }

  // 오프셋 기반 페이지네이션
  default <T> PageResponse<T> fromPage(Page<T> page, Object nextCursor) {
    return new PageResponse<>(
        page.getContent(),
        nextCursor,
        page.getSize(),
        page.hasNext(),
        page.getTotalElements() // Page 객체 총 개수 제공 O
    );
  }
}