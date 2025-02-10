package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.HashMap;
import java.util.UUID;

public interface UserService {
    UUID createUser(String userName, String email,String password);

    //UUID createUser(String userName, String email,String password, String profilePicturePath);

    HashMap<UUID, User> getUsersMap();

    User getUserById(UUID id);

    String getUserNameById(UUID id);

    boolean deleteUser(UUID userId);

    boolean changeUserName(UUID userId, String newName);

    boolean isUserExist(UUID userId);

}

