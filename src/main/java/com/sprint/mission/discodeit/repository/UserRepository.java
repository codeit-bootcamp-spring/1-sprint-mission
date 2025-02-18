package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository {

    User save(User user);

    User findById(UUID userId);

    User findByName(String name);

    List<User> findAll();

    void updateUser(User user);

    void deleteUser(UUID userId);
}
