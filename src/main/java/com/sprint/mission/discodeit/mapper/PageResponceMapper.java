package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.Cursor;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

public class PageResponceMapper {

  public static <T> PageResponse<T> fromPage(Page<T> page){
    return new PageResponse<>(
        page.getContent(),
        null,
        page.getNumber(),
        page.getSize(),
        page.hasNext(),
        page.getTotalElements()
    );
  }
  public static <T> PageResponse<T> fromSlice(Slice<T> slice){
    return new PageResponse<>(
        slice.getContent(),
        null,
        slice.getNumber(),
        slice.getSize(),
        slice.hasNext(),
        null
    );
  }
  public static <T> PageResponse<T> fromCursor(Cursor<T> cursor, int size) {
    return new PageResponse<>(
        cursor.content(),
        cursor.nextCursor(),
        0,
        size,
        cursor.hasNext(),
        null
    );
  }
}
