package com.sprint.mission.discodeit.service;

import project.entity.User;

import java.util.List;

public interface UserService {
    void createUser(User user);
    User readUser(String userId);
    List<User> readAllUsers();
    void updateUser(String userId, String newUserName);
    void deleteUser(String userId);
}
