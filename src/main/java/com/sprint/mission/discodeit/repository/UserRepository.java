package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository {

    UUID save(User user);
    User findOne(UUID id);
    List<User> findAll();
    UUID update(User user);
    UUID delete(UUID id);
}
