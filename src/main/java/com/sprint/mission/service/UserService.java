package com.sprint.mission.service;


import com.sprint.mission.entity.User;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public interface UserService {
    User create(User user) ;
    User update(User user);
    User findById(UUID id);
    List<User> findAll();
    void delete(User user);
    //void validateDuplicateName(String name);
}
