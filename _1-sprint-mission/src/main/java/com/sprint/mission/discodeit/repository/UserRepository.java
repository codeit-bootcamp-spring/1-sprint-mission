package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public interface UserRepository {
    User create(User user);
    User findById(UUID userId);
    List<User> findAll();
    void delete(UUID userId);
}
