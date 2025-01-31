package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class FileUserService implements UserService {
    private final FileUserRepository repository;

    public FileUserService() {
        this.repository = new FileUserRepository();
    }

    @Override
    public void addUser(User user) {
        if (repository.findAll().stream().anyMatch(u -> u.getUsername().equals(user.getUsername()))) {
            throw new IllegalArgumentException("이미 존재하는 사용자입니다.");
        }
        repository.save(user);
    }

    @Override
    public User getUser(UUID id) {
        User user = repository.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public void updateUser(UUID id, String newUsername, String newPassword) {
        User user = repository.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }
        user.setUsername(newUsername);
        user.setPassword(newPassword);
        repository.update(id, user);
    }

    @Override
    public void deleteUser(UUID id) {
        if (repository.findById(id) == null) {
            throw new IllegalArgumentException("존재하지 않는 사용자입니다.");
        }
        repository.delete(id);
    }
}
