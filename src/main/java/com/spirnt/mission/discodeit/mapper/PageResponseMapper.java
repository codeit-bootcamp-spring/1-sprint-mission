package com.spirnt.mission.discodeit.mapper;

import org.springframework.stereotype.Component;

// MapStruct는 매핑 시 타입을 정확히 알아야 해서 제너릭을 쓸 수 없음 -> component로
@Component
public class PageResponseMapper<T> {

//  public PageResponse<T> fromSlice(Slice<T> slice) {
//    return new PageResponse<>(slice.getContent(), slice.getNumber(), slice.getSize(),
//        slice.hasNext(),
//        (long) slice.getNumberOfElements());
//  }

//  public PageResponse<T> fromPage(Page<T> page) {
//    return new PageResponse<>(page.getContent(), page.getNumber(), page.getSize(),
//        page.hasNext(), page.getTotalElements());
//  }
}