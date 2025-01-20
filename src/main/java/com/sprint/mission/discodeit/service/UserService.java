package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    public User createUser(String userName);
    User readUser(UUID userId);
    List<User> readAllUser();
    public User modifyUser(UUID userID, String newName);
    public void deleteUser(UUID userID);

}
