package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class BasicUserService implements UserService {
    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(String username) {
        return userRepository.save(new User(username));
    }

    @Override
    public Map<UUID, User> getUsers() {
        return userRepository.getAllUsers().stream()
                .collect(Collectors.toMap(User::getId, user -> user));
    }

    @Override
    public Optional<User> getUser(UUID id) {
        return Optional.ofNullable(userRepository.getUserById(id));
    }

    @Override
    public Optional<User> updateUser(UUID id, String newUsername) {
        return Optional.ofNullable(userRepository.getUserById(id))
                .map(user -> {
                    user.updateUsername(newUsername);
                    return userRepository.save(user);
                });
    }

    @Override
    public Optional<User> deleteUser(UUID id) {
        return Optional.ofNullable(userRepository.getUserById(id))
                .map(user -> {
                    userRepository.deleteById(id);
                    userRepository.save();
                    return user;
                });
    }
}
