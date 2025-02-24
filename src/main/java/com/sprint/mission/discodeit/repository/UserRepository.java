package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.web.dto.UserUpdateDto;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User createUser(User user);
    void updateUser(UUID id, UserUpdateDto userParam);
    void deleteUser(UUID id);
    Optional<User> findById(UUID id);
    Optional<User> findByloginId(String loginId);
    List<User> findUsersInChannel(Channel channel);
    List<User> findAll();
}
