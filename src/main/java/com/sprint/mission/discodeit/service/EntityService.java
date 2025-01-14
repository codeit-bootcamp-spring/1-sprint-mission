package com.sprint.mission.discodeit.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// CRUD 작업을 위한 제네릭 서비스 인터페이스
public interface EntityService<T> {
    T create(T entity); // 객체 생성
    Optional<T> read(UUID id); // id를 기반으로 객체 조회
    List<T> readAll(); // 모든 객체 조회
    T update(UUID id, T entity); // 객체 수정
    boolean delete(UUID id); // 객체 삭제
}