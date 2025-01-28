package com.sprint.misson.discordeit.repository.file;

import com.sprint.misson.discordeit.entity.User;
import com.sprint.misson.discordeit.repository.UserRepository;
import com.sprint.misson.discordeit.service.file.FileService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class FileUserRepository implements UserRepository {

    private final Path userDirectory;

    public FileUserRepository() {
        this.userDirectory = Paths.get(System.getProperty("user.dir")).resolve("data/user/");
        FileService.init(userDirectory);
    }

    @Override
    public User save(User user) {
        Path userPath = userDirectory.resolve(user.getId().concat(".ser"));
        FileService.save(userPath, user);
        return user;
    }

    @Override
    public boolean delete(User user) {
        Path userPath = userDirectory.resolve(user.getId().concat(".ser"));
        return FileService.delete(userPath);
    }

    @Override
    public User findById(String id) {
        return findAll().stream().filter(user -> user.getId().equals(id)).findFirst().orElse(null);
    }


    @Override
    public List<User> findAll() {
        return FileService.load(Paths.get(userDirectory.toString()));
    }
}
