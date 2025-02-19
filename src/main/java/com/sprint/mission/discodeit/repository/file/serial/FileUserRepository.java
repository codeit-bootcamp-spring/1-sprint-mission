package com.sprint.mission.discodeit.repository.file.serial;

import com.sprint.mission.discodeit.entity.user.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.file.FileService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FileUserRepository implements UserRepository, FileService<User> {
    public static final String USER_PATH = "user.ser";

    private Map<UUID, User> userMap = new HashMap<>();

    @Override
    public User userSave(User user) {
        userMap.put(user.getId(), user);

        save(USER_PATH, userMap);

        return userMap.get(user.getId());
    }

    @Override
    public User findUserById(UUID userId) {
        return userMap.get(userId);
    }

    @Override
    public Map<UUID, User> findAllUser() {
        return load(USER_PATH, userMap);
    }

    @Override
    public void removeUserById(UUID userId) {
        userMap.remove(userId);

        save(USER_PATH, userMap);
    }
}
