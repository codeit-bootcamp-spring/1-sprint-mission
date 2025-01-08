package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    public void Creat(User user);

    public void Delete(User user);

    public void Update(User user, User updateUser);

    public List<User> Write(UUID id);

    public List<User> AllWrite();
}
