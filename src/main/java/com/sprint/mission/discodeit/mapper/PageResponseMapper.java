package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public class PageResponseMapper {

  /**
   * Slice 객체를 PageResponse로 변환 (오프셋 페이징용)
   */
  public <T> PageResponse<T> fromSlice(Slice<T> slice) {
    return new PageResponse<>(
        slice.getContent(),
        null,  // 커서 방식이 아니므로 nextCursor는 null
        slice.getSize(),
        slice.hasNext(),
        null  // totalElements 알 수 없음
    );
  }

  /**
   * Page 객체를 PageResponse로 변환 (오프셋 페이징용)
   */
  public <T> PageResponse<T> fromPage(Page<T> page) {
    return new PageResponse<>(
        page.getContent(),
        null,  // 커서 방식x
        page.getSize(),
        page.hasNext(),
        page.getTotalElements()
    );
  }

  /**
   * 커서 기반 페이징을 위한 PageResponse 생성
   *
   * @param content    페이지 컨텐츠
   * @param nextCursor 다음 페이지 조회를 위한 커서
   * @param size       페이지 크기
   * @param hasNext    다음 페이지 존재 여부
   * @param <T>        컨텐츠 타입
   * @return PageResponse 객체
   */
  public <T> PageResponse<T> fromCursorResult(
      List<T> content,
      Object nextCursor,
      int size,
      boolean hasNext
  ) {
    return new PageResponse<>(
        content,
        nextCursor,
        size,
        hasNext,
        null  // 커서 페이징에서는 totalElements를 계산하지 않음
    );
  }
}
