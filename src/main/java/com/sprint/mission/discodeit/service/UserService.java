package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.log.MyLog;

import java.util.List;

public interface UserService {
    MyLog<User> createUser(String name, String phone, String password);

    MyLog<User>  getUserById(String phone);

    List<User> getAllUser();

    MyLog<User> updateUserPassword(String phone, String newPass);

    MyLog<User> deleteUser(User user);

}
