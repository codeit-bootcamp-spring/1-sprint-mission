package com.spirnt.mission.discodeit.repository;

import com.spirnt.mission.discodeit.enity.User;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    // 데이터 삭제
    void save(User user);

    // 데이터 삭제
    void delete(UUID userId);

    // 객체 찾기
    Optional<User> findById(UUID userId);

    // 저장된 모든 데이터 가져오기
    Map<UUID, User> findAll();

    // 존재 검증
    boolean existsById(UUID userId);
}
