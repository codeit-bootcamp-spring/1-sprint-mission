package com.srint.mission.discodeit.service;

import com.srint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UUID create(User user);

    User read(UUID id);

    List<User> readAll();

    User update(UUID id, User user);

    UUID delete(UUID id);
}
