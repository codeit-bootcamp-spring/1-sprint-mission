package com.sprint.mission.discodeit.dto.response;

import java.util.List;
import lombok.Builder;

@Builder
public record PageResponse<T>(
    List<T> content,
    int number,
    int size,
    boolean hasNext,
    Long totalElements
) {

}
