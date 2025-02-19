package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


public interface UserRepository {
    void save(User user);

    User findByUsername(String username);

    User findByUserId(UUID id);

    List<User> findAll();

    void deleteById(UUID id);
}
