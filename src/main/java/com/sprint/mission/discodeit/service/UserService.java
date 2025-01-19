package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import java.util.HashMap;
import java.util.UUID;

public interface UserService {
    HashMap<UUID, User> getUsersMap();

    UUID createUser(String userName);

    User getUser(UUID userId);

    boolean deleteUser(UUID userId);

    boolean isUserExist(UUID userID);

    boolean changeUserName(UUID userID, String newName);
}
