package com.srint.mission.discodeit.service;

import com.srint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    //DB로직
    UUID save(User user);
    User findOne(UUID id);
    List<User> findAll();
    UUID delete(UUID id);

    UUID create(String username, String email, String password);
    User read(UUID id);
    List<User> readAll();
    User updateUserName(UUID id, String name);
    User updateEmail(UUID id, String email);
    User updatePassword(UUID id, String password);
    UUID deleteUser(UUID id);
}
