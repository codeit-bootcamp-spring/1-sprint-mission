package com.sprint.mission.discodeit.dto.page;

import java.util.List;

public record PageResponse<T>(
    List<T> content, //실제 데이터
    Object nextCursor, //다음 커서 값
    int size, // 페이지 크기
    boolean hasNext,
    Long totalElements //T 데이터의 총 갯수, null일 수 있
) {

}
