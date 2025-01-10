package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser(String name, String nickname, String email, String password, String profileImageUrl);
    User findUserById(UUID id);
    List<User> findAllUsers();
    void updateUser(UUID id, String name, String nickname, String email, String password, String profileImageUrl);
    void deleteUser(UUID id);
}
