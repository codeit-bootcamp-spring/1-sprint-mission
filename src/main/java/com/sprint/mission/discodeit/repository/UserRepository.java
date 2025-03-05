package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User saveUser(User user);

    User findUserById(UUID userId);

    List<User> findAllUsers();

    void removeUser(UUID userId);

    boolean existsUser(UUID userId);

    Optional<User> findUserByName(String name);

    Optional<User>  findUserByEmail(String email);

    Optional<User>  findUserByNameAndPassword(String name, String password);

}
