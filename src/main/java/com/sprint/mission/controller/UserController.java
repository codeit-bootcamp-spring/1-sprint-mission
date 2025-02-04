package com.sprint.mission.controller;

import com.sprint.mission.entity.User;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public interface UserController {


    User create(String name, String password);
    User updateUserNamePW(UUID id, String newName, String password);

    User findById(UUID id);
    Set<User> findAll();
    Set<User> findUsersByName(String findName);
    User findUserByNamePW(String name, String password);
    Set<User> findUsersInChannel(UUID channelId);

    void deleteUser(UUID id, String nickName, String password);
    void drops(UUID channel_Id, UUID droppingUser_Id) throws IOException;
    void dropsAllByUser(UUID droppingUser_Id) throws IOException;
    void addChannelByUser(UUID channelId, UUID userId) throws IOException;

    void createUserDirectory();
}
