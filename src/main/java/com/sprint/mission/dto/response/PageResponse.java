package com.sprint.mission.dto.response;

import org.springframework.data.domain.Slice;

import java.util.List;

public record PageResponse <T>(
        List<T> content,
        int number,
        int size,
        boolean hasNext,
        Long totalElements) {

    public PageResponse<T> fromSlice(Slice<T> slice){
        return new PageResponse<>(
                slice.getContent(),
                slice.getNumber(),
                slice.getSize(),
                slice.hasNext(),
                slice.getNumberOfElements() == 0 ? 0L : slice.getNumberOfElements()
        );
    }

}
