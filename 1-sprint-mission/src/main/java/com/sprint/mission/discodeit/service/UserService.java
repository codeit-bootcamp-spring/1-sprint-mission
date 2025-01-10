package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void create(User user);
    Optional<User> findById(String id);
    List<User> findAll();
    void update(String id, String newUsername);
    void delete(String id);
}
