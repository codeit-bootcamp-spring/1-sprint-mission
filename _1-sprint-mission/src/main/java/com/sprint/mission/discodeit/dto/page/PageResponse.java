package com.sprint.mission.discodeit.dto.page;

import java.util.List;

public record PageResponse<T>(
    List<T> content, //실제 데이터
    int number,//페이지 번호
    int size, // 페이지 크기
    boolean hasNext,
    Long totalElements //T 데이터의 총 갯수, null일 수 있
) {

}
