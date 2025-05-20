package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;

@Component
public class PageResponseMapper {

    public static <T> PageResponse<T> fromSlice(Slice<T> slice, Object nextCursor) {
        return PageResponse.<T>builder()
            .content(slice.getContent())
            .nextCursor(nextCursor)
            .size(slice.getSize())
            .hasNext(slice.hasNext())
            .build();
    }

    public static <T> PageResponse<T> fromPage(Page<T> page, Object nextCursor) {
        return PageResponse.<T>builder()
            .content(page.getContent())
            .nextCursor(nextCursor)
            .size(page.getSize())
            .hasNext(page.hasNext())
            .totalElements(page.getTotalElements())
            .build();
    }
}
