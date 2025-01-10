package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.UUID;

public interface BaseService<T>{
    //CRUD(생성, 읽기, 모두 읽기, 수정, 삭제)

    // 생성
    T create(T t);
    // 읽기
    T readById(UUID id);
    // 모두 읽기
    List<T> readAll();
    // 수정
    T update(UUID id, T t);
    // 삭제
    void delete(UUID id);
}
