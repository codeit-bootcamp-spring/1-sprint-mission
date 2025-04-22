package com.sprint.mission.discodeit.dto.page;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

@Builder
@AllArgsConstructor
public class PageResponse<T> {
    public List<T> content;
    //    public int number;
    public Object nextCursor;
    public int size;
    public boolean hasNext;
    public Long totalElements;
}
