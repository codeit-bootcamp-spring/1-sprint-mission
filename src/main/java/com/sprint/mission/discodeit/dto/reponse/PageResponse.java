package com.sprint.mission.discodeit.dto.reponse;

import java.util.List;
import lombok.Builder;

@Builder
//  제네릭 타입임을 명시해야 한다.
public record PageResponse<T>(
    List<T> content, // 실제 데이터
    int number, // 페이지 번호
    int size, // 페이지의 크기
    boolean hasNext,
    Long totalElements // T 데이터의 총 갯수, null일 수 있다. <- 필요없다고 했을 때 null 지정
) {

}
