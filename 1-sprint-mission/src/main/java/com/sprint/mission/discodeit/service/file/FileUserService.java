package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.UUID;

public class FileUserService implements UserService {
    private final UserRepository userRepository;

    public FileUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(String name, String email, String password) {
        String userId = UUID.randomUUID().toString();
        User user = new User(userId, name, email, password);
        return userRepository.save(user);
    }

    @Override
    public User findById(String userId) {
        return userRepository.findById(userId);
    }
}
