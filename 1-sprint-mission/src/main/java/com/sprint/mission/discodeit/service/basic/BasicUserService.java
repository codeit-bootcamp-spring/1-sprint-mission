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
    public User create(User user) {
        // ID 설정
        if (user.getId() == null) {
            user.setId(UUID.randomUUID());
        }
        return userRepository.save(user);
    }

    @Override
    public User findById(String id) {
        try {
            UUID uuid = UUID.fromString(id);
            return userRepository.findById(uuid);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid UUID format: " + id, e);
        }
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(String id, User updated) {
        try {
            UUID uuid = UUID.fromString(id);
            User existing = userRepository.findById(uuid);
            if (existing == null) {
                throw new RuntimeException("User not found for ID: " + id);
            }
            existing.update(updated.getUsername(), updated.getEmail());
            userRepository.save(existing);
            return existing;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid UUID format: " + id, e);
        }
    }

    @Override
    public void delete(String id) {
        try {
            UUID uuid = UUID.fromString(id);
            userRepository.delete(uuid);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid UUID format: " + id, e);
        }
    }
}
