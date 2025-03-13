package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.reponse.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

// MapStruct에서 제네릭 타입을 제대로 처리하지 못하는 이슈가 있어(강사님께서는 가능하다고 하셨지만)
// 해당 매퍼는 직접 Java 코드로 구현했습니다.
@Component
public class PageResponseMapper<T> {

  public PageResponse<T> fromPage(Page<T> page) {
    PageResponse<T> response = new PageResponse<>(page.getContent(),
        page.getNumber(),
        page.getSize(),
        page.hasNext(),
        null);
    return response;
  }
}
