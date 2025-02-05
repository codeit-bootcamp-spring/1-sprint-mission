package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.io.File;
import java.util.List;
import java.util.UUID;

public interface UserRepository {

    User save(String userName, String email);

    User findUserById(UUID id);

    List<User> findAll();

    boolean delete(UUID id);

    void update(UUID id, String username, String email);
}

