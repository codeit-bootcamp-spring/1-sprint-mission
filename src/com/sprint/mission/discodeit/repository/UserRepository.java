package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public interface UserRepository {
    User createUser(User userToCreate);
    User findUserById(UUID key);
    User updateUserById(UUID key, User userToUpdate);
    User deleteUserById(UUID key);
}
