package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface UserService {
  User saveUser(User user);
  User update(User user);
  User findUserById(String id);
  List<User> findAllUsers();
  User updateUser(String id, UserUpdateDto updatedUser);

  void deleteUser(String id);

}
