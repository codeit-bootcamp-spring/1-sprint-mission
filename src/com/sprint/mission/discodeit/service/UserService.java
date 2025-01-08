package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface UserService {
    public void createUser(User user);
    User readUser(String userId);
    List<User> readAllUser();
    public void modifyUser(String userID);
    public void deleteUser(String userID);
}
