package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    void save(User user); // 사용자 저장
    Optional<User> findById(UUID id); // ID로 사용자 찾기
    Optional<User> findByUsername(String username); // 사용자명으로 찾기
    Optional<User> findByEmail(String email); // 이메일로 찾기
    List<User> findAll(); // 모든 사용자 조회
    void deleteById(UUID id); // ID로 사용자 삭제
}
