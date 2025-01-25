package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class FileUserService implements UserService {
    private final UserRepository repository;

    public FileUserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User createUser(String username, String email) {
        User user = new User(username, email);
        repository.save(user);
        return user;
    }

    @Override
    public User getUser(UUID id) {
        return repository.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public User updateUser(UUID id, String username, String email) {
        User user = repository.findById(id);
        if (user != null) {
            user.update(username, email);
            repository.save(user);
            return user;
        }
        return null;
    }

    @Override
    public void deleteUser(UUID id) {
        repository.delete(id);
    }
}
