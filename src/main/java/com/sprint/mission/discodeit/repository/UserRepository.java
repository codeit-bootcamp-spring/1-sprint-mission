package com.sprint.mission.discodeit.repository;
import com.sprint.mission.discodeit.entity.User;
import java.util.Map;
import java.util.UUID;

public interface UserRepository {
    // 저장
    void saveUser(User user);
    // 읽기 = 찾기
    User findById(UUID id);
    Map<UUID, User> getAllUsers();
    // 삭제
    void deleteAllUsers();
    void deleteUserById(UUID id);
}