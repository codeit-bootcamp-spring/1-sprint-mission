package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.CursorResponse;
import com.sprint.mission.discodeit.entity.BaseEntity;
import java.time.Instant;
import java.util.List;
import java.util.function.Function;
import org.springframework.data.domain.Page;

public class CursorResponseMapper {

  public static <T extends BaseEntity> CursorResponse<T> fromPage(Page<T> page) {
    List<T> content = page.getContent();

    Instant nextCursor = content.isEmpty() ? null : content.get(content.size() - 1).getCreatedAt();

    return new CursorResponse<>(
        content,
        nextCursor,
        page.getSize(),
        page.hasNext()
    );
  }
}
