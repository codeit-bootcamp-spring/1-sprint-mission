package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.entity.User;
import com.sprint.mission.discodeit.dto.form.UserUpdateDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User createUser(UUID id, User user);
    void updateUser(UUID id, UserUpdateDto userParam);
    void deleteUser(UUID id);
    Optional<User> findById(UUID id);
    Optional<User> findByloginId(String loginId);
    List<User> findAll();
}
