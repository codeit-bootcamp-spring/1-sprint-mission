package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User createUser(String name, String email, String iD ,String password);

    User find(UUID uuid);

    List<User> findAll();

    User update(UUID uuid, String email, String iD, String password);

    void delete(UUID uuid);
}
