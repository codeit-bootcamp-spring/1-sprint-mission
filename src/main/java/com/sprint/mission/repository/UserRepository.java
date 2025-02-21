package com.sprint.mission.repository;

import com.sprint.mission.entity.main.User;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);
    Optional<User> findById(UUID id);
    List<User> findAll();
    void delete(UUID userId);
    boolean existsById(UUID userId);
    //User updateUserNamePW(User user)
}
