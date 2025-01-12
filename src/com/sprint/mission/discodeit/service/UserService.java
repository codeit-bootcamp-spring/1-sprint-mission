package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public interface UserService {
    User createUser(User userToCreate);
    User findUserById(UUID key);
    User updateUserById(UUID key, User userToUpdate);
    User deleteUserById(UUID key);
}
