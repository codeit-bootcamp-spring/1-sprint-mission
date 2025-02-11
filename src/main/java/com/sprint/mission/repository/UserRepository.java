package com.sprint.mission.repository;

import com.sprint.mission.entity.main.User;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user) throws IOException;
    User findById(UUID id) throws IOException, ClassNotFoundException;
    List<User> findAll() throws IOException;
    void delete(UUID userId);

    //User updateUserNamePW(User user) throws IOException;
}
