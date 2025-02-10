package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public interface UserService {
    User createUser(User userInfoToCreate);
    User findUserById(UUID key);
    User updateUserById(UUID key, User userInfoToUpdate);
    User deleteUserById(UUID key);
}
