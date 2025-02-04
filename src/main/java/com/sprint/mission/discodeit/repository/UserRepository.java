package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    void save(User user);  // 사용자 저장

    Optional<User> findById(UUID id);  // 특정 사용자 조회

    List<User> findAll();  // 모든 사용자 조회

    void update(UUID id, User user);  // 사용자 수정

    void delete(UUID id);  // 사용자 삭제
}
