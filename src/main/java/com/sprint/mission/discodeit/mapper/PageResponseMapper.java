package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import org.mapstruct.Mapper;
import java.util.List;

@Mapper(componentModel = "spring")
public interface PageResponseMapper {

  // 커서 기반 페이지네이션 데이터를 PageResponse로 변환
  static <T> PageResponse<T> toCursorPageResponse(List<T> content, int size, boolean hasNext,
      String nextCursor) {
    PageResponse<T> response = new PageResponse<>();
    response.setContent(content);
    response.setSize(size);
    response.setHasNext(hasNext);
    response.setNextCursor(nextCursor);
    return response;
  }
}