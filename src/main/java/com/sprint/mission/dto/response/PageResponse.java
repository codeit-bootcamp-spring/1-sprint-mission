package com.sprint.mission.dto.response;

import java.util.List;

public record PageResponse <T>(
        List<T> content,
        int number,
        int size,
        boolean hasNext,
        Long totalElements) {
}
