package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import java.util.*;
public interface UserService {

    void createUser(User user);

    User readUser(String id);

    void updateUser(User user);

    void deleteUser(String id);

    List<User> readAllUsers(); // 모든 사용자 반환

}
