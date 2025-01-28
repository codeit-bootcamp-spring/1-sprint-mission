package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.UUID;

public interface UserRepository {
    User save(User newUser);

    List<User> getAllUsers();

    User getUserById(UUID id);

    void deleteById(UUID id);

    void save();
}
