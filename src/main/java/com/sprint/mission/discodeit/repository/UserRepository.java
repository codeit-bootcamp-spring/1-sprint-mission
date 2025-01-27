package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    //생성
    void createUser(User user);

    //읽기
    Optional<User> getUserById(UUID id);

    //모두 읽기
    List<User> getAllUsers();

    //수정
    void updateUser(UUID id,User user);

    //삭제
    void deleteUser(UUID id);
}
