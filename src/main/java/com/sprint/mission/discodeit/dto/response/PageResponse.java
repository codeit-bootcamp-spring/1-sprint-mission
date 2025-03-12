package com.sprint.mission.discodeit.dto.response;

import java.util.List;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponse<T> {

  //실제 데이터
  private List<T> contents;
  //페이지 번호
  //private int number;
  //다음 페이지 커서 객체
  private Objects nextCursor;
  //페이지 크기
  private int size;
  //다음 페이지가 있는지 여부
  private boolean hasNext;
  //T 데이터의 총 개수, null일 수 있음
  private Long totalElements;
}
