package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.UsersDto;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    User save(User user);
    void deleteById(String id);
    Optional<User> findById(String id);
    List<UsersDto> findAll();

}