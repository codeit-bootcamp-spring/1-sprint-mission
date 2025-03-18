package com.sprint.mission.discodeit.dto.response;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;

import java.util.List;

//public record PageResponse<T>(
//        List<T> content,
//        int number,
//        int size,
//        boolean hasNext,
//        Long totalElements
//) {
//}

public class PageResponse<T> {

    private final List<T> content;
    private final int number;
    private final int size;
    private final Integer totalElements; // null 가능

    public PageResponse(Page<T> page) {
        this.content = page.getContent();
        this.number = page.getNumber();
        this.size = page.getSize();
        this.totalElements = (int) page.getTotalElements(); // Page에서는 총 개수를 제공
    }

    public PageResponse(Slice<T> slice) {
        this.content = slice.getContent();
        this.number = slice.getNumber();
        this.size = slice.getSize();
        this.totalElements = null; // Slice는 전체 개수를 알 수 없음
    }
}