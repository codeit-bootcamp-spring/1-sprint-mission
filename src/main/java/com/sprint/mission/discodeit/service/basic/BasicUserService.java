package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class BasicUserService implements UserService {
    private static volatile BasicUserService instance;
    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    protected static BasicUserService getInstance(UserRepository userRepository) {
        if (instance == null) {
            synchronized (BasicUserService.class) {
                if (instance == null) {
                    instance = new BasicUserService(userRepository);
                }
            }
        }
        return instance;
    }

    @Override
    public User register(User user) {
        userRepository.save(user);
        return user;
    }

    @Override
    public Optional<User> getUserDetails(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean updateUserProfile(UUID id, String name, String email, UserStatus status) {
        Optional<User> byId = userRepository.findById(id);
        if(byId.isPresent()){
            User user = byId.get();
            user.update(name, email, status);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteUser(UUID id) {
        return userRepository.delete(id);
    }
}
