package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.HashMap;
import java.util.UUID;

public interface UserService {

    User CreateUser(String name, String email,String iD ,String password);

    User getUser(UUID uuid);

    HashMap<UUID, User> getAllUsers();

    void updateUser(UUID uuid, String email, String iD, String password);

    void deleteUser(UUID uuid);
}
