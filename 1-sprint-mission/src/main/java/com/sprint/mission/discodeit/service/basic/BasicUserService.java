package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class BasicUserService implements UserService {

    private final UserRepository userRepository;

    public BasicUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(String username, String email) {
        User user = new User(username, email);
        return userRepository.save(user);
    }

    @Override
    public User findById(UUID id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(UUID id, String newUsername, String newEmail) {
        User existing = userRepository.findById(id);
        if (existing != null) {
            existing.update(newUsername, newEmail);
            return userRepository.save(existing);
        }
        return null;
    }

    @Override
    public void delete(UUID id) {
        userRepository.delete(id);
    }
}
