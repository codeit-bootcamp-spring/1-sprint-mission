package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

public interface UserService {

    void registerUser(User user);
    void updateUserName(User user, String name);
    void updateUserEmail(User user, String email);
    void deleteUser(String name, String password);
    void getUserInfo(String name);
    void getAllUser();
}
