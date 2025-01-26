package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.Map;
import java.util.UUID;

public interface UserRepository {
    public void save(Map<UUID, User> userList);
    public Map<UUID, User> load();
}
