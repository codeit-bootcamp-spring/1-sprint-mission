package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;


public interface UserRepository {
    User save(User user);

    User findByUsername(String username);

    User findByUserId(UUID id);

    User findByEmail(String email);

    List<User> findAll();

    boolean existsById(UUID id);

//    boolean existsByEmail(String email);
//
//    boolean existsByUsername(String username);

    void deleteById(UUID id);
}
