package com.sprint.mission.controller;

import com.sprint.mission.entity.User;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UserController {


    User create(String name, String password, String email);
    User updateAll(UUID id, String newName, String password, String email);

    User findById(UUID id);
    List<User> findAll();
    List<User> findUsersByName(String findName);
    User findUserByNamePW(String name, String password);

    void deleteUser(UUID id, String nickName, String password);
    void drops(UUID channel_Id, UUID droppingUser_Id) throws IOException;
    void dropsAllByUser(UUID droppingUser_Id) throws IOException;
    void addChannelByUser(UUID channelId, UUID userId) throws IOException;

    void createUserDirectory();
}
