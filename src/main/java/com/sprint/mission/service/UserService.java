package com.sprint.mission.service;


import com.sprint.mission.entity.User;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UserService {
    User create(User user) throws IOException;
    User update(User user);
    User findById(UUID id);
    List<User> findAll();
    void delete(User user);
    //void validateDuplicateName(String name);
}
