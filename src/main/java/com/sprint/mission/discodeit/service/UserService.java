package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User createUser (UUID id, String userName);

    User getUser(UUID id);

    List<User> allUsers();

    void updateUser(UUID id, String newUserName);

    void deleteUser(UUID id);



}
