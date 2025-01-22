package com.sprint.mission.discodeit.repository;
import com.sprint.mission.discodeit.entity.User;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    // 저장
    void saveUser(User user);
    // 읽기 = 찾기
    Optional<User> findUserById(UUID id);
    Collection<User> getAllUsers();
    // 삭제
    void deleteAllUsers();
    void deleteUserById(UUID id);
}