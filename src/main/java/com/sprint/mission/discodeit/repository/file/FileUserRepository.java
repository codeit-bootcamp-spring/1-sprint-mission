package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileUserRepository implements UserRepository {

    private final FileStorage fileStorage;
    private final Path directory;
    private static final String SUBDIRECTORY = "user";


    public FileUserRepository(FileStorage fileStorage) {
        this.fileStorage = fileStorage;
        directory = fileStorage.getDirectory(SUBDIRECTORY);
        fileStorage.init(directory);
    }

    @Override
    public User saveUser(User user) {
        Path filePath = fileStorage.getFilePath(directory, user.getId());
        return fileStorage.save(filePath, user);
    }

    @Override
    public User findUserById(UUID userId) {
        List<User> userList = fileStorage.load(directory);

        return userList.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst().orElse(null);
    }

    @Override
    public List<User> findAllUsers() {
        return fileStorage.load(directory);
    }

    @Override
    public void removeUser(UUID userId) {
        User user = findUserById(userId);
        fileStorage.remove(directory, user);
    }

    @Override
    public boolean existsUser(UUID userId) {
        List<User> userList = fileStorage.load(directory);

        return userList.stream()
                .anyMatch(user -> user.getId().equals(userId));
    }

    @Override
    public Optional<User> findUserByName(String name) {
        List<User> userList = fileStorage.load(directory);

        return userList.stream()
                .filter(user -> user.getName().equals(name))
                .findFirst();
    }

    @Override
    public Optional<User>  findUserByEmail(String email) {
        List<User> userList = fileStorage.load(directory);

        return userList.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Optional<User>  findUserByNameAndPassword(String name, String password) {
        List<User> userList = fileStorage.load(directory);

        return userList.stream()
                .filter(user -> user.getName().equals(name) && user.getPassword().equals(password))
                .findFirst();
    }
}
