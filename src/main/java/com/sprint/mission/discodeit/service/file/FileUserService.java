package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileUserService implements UserService {
    private final UserRepository repository;

    public FileUserService(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void createUser(User user) {
        repository.save(user);
        System.out.println("[User Created]: " + user);
    }

    @Override
    public Optional<User> getUser(UUID id) {
        return repository.findById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return repository.findAll();
    }

    @Override
    public void updateUser(User user) {
        repository.save(user);
        System.out.println("[User Updated]: " + user);
    }

    @Override
    public void deleteUser(UUID id) {
        repository.delete(id);
        System.out.println("[User Deleted]");
    }
}
