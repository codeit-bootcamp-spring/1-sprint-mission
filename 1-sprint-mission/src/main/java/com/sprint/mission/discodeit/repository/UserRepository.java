package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface UserRepository {
    void save(User user);
    User findById(String userId);
    List<User>findAll();
    void delete(String userId);

}
