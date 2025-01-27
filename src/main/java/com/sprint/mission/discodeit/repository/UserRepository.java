package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);

    Optional<User> findByPhone(String phone);

    List<User> findAll();

    User delete(User user);
}
