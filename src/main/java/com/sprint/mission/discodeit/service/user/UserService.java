package com.sprint.mission.discodeit.service.user;


import com.sprint.mission.discodeit.dto.user.RoleUpdateRequest;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public interface UserService {

  User saveUser(User user);

  User update(User user);

  User findUserById(String id);

  List<User> findAllUsers();

  List<User> findAllUsersIn(List<String> userIds);

  List<User> findByAllIn(List<UUID> userIds);

  void deleteUser(String id);


  User updateUserRole(RoleUpdateRequest request);

}
