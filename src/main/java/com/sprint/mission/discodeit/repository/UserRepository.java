package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;

public interface UserRepository {

    User save(User user);

    boolean delete(String userId);

    User findById(String id);

    User findByUsername(String username);

    User findByEmail(String email);

    List<User> findAll();

}
