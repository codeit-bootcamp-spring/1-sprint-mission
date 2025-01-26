package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public interface UserRepository {

    UUID save(String userName);
    User findUserById(UUID id);
    List<User> findAll();
    boolean delete(UUID id);
    void update(UUID id, String username);
}

