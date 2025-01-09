package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;

public interface UserService {
    User create(User user);
    User findById(String id);
    List<User> findAll();
    User update(String id, User user);
    void delete(String id);
}
