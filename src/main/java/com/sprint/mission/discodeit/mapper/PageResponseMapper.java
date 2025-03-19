package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public class PageResponseMapper {

  /**
   * Slice 객체로부터 PageResponse DTO를 생성합니다.
   *
   * @param slice 변환할 Slice 객체
   * @param <T>   데이터 타입
   * @return 생성된 PageResponse 객체
   */
  public <T> PageResponse<T> fromSlice(Slice<T> slice) {
    return new PageResponse<>(
        slice.getContent(),
        slice.getNumber(),
        slice.getSize(),
        slice.hasNext(),
        null  // Slice는 총 요소 수를 알 수 없음
    );
  }

  /**
   * Page 객체로부터 PageResponse DTO를 생성합니다.
   *
   * @param page 변환할 Page 객체
   * @param <T>  데이터 타입
   * @return 생성된 PageResponse 객체
   */
  public <T> PageResponse<T> fromPage(Page<T> page) {
    return new PageResponse<>(
        page.getContent(),
        page.getNumber(),
        page.getSize(),
        page.hasNext(),
        page.getTotalElements()
    );
  }
}