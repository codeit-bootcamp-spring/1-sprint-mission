package com.sprint.mission.discodeit.repository;
import com.sprint.mission.discodeit.entity.User;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    // 저장
    User saveUser(User user);
    // 읽기 = 찾기
    // TODO : 레포지토리 관점에서 네이밍 더 간단하게 만들기
    Optional<User> findUserById(UUID id);
    Collection<User> getAllUsers();
    // 삭제
    void deleteUserById(UUID id);
}