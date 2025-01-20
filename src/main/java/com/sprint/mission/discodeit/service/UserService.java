package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User createUser(String name, String email);

    void updateUserName(User user, String name);
    void updateUserEmail(User user, String email);

    List<User> getAllUserList();
    User searchById(UUID userId);

    void printUserInfo(User user);
    void printUserListInfo(List<User> userList);

    void deleteUser(User user);

}
