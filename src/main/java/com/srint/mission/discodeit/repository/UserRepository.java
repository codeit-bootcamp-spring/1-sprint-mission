package com.srint.mission.discodeit.repository;

import com.srint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserRepository {

    UUID save(User user);
    User findOne(UUID id);
    List<User> findAll();
    UUID delete(UUID id);
}
