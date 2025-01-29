package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.Map;

public interface UserRepository {

    public Long saveUser(User user);

    public User loadUser(Long id);

    public Map<Long, User> loadAllUsers();

    public void deleteUser(Long id);

    public void updateUser(Long id, User user);
}
