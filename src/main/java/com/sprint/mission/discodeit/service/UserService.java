package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

/*
    //DB로직
    UUID save(User user);
    User findOne(UUID id);
    List<User> findAll();
    UUID update(User user);
    UUID delete(UUID id);
*/

    UUID create(String username, String email, String password);
    User read(UUID id);
    List<User> readAll();
    User updateUser(UUID id, String name, String email);
    UUID deleteUser(UUID id);
}
