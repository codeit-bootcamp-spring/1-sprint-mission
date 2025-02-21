package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository {
    User createUser(User userInfoToCreate);
    User findUserById(UUID key);
    List<User> findAllUser();
    User findUserByName(String name);
    User updateUserById(UUID key, User userInfoToUpdate);
    User deleteUserById(UUID key);
}
