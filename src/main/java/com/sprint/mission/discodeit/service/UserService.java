package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.UUID;

public interface UserService {
    void createUser(User user);
    User readUserById(UUID userId);
    void updateUserField(UUID userId, String fieldName, String contents);
    void deleteUserById(UUID userId);

}
