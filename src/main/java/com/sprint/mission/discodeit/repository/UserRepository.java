package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface UserRepository {

    User save(User user);

    boolean delete(User user);

    User findById(String id);

    List<User> findAll();

}
