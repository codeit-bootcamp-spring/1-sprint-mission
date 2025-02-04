package com.sprint.mission.service;


import com.sprint.mission.entity.User;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public interface UserService {
    User createOrUpdate(User user) throws IOException;
    User update(User user);
    User findById(UUID id);
    Set<User> findAll();
    void delete(User user);
    //void validateDuplicateName(String name);
}
