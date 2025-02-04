package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class BasicUserService implements UserService {
    private final UserRepository repository;
    private static volatile BasicUserService instance;

    public BasicUserService(UserRepository repository) {
        this.repository = repository;
    }
    public static BasicUserService getInstance(UserRepository repository) {
        if (instance == null) {
            synchronized (BasicUserService.class) {
                if (instance == null) {
                    instance = new BasicUserService(repository);
                }
            }
        }
        return instance;
    }
    @Override
    public UUID createUser(String userName) {
        return repository.save(userName);
    }

    @Override
    public User getUser(UUID id) {
        return repository.findUserById(id);
    }

    @Override
    public List<User> getUsers() {
        return repository.findAll();
    }

    @Override
    public void updateUser(UUID id, String userName) {
        repository.update(id, userName);
    }

    @Override
    public void deleteUser(UUID id) {
        repository.delete(id);
    }
}
