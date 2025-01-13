package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class FileUserService implements UserService {
    private final FileStorage fileStorage;
    private final Path directory = Paths.get(System.getProperty("user.dir"), "data", "user");

    public FileUserService(FileStorage fileStorage) {
        this.fileStorage = fileStorage;
        fileStorage.init(directory);
    }

    @Override
    public User createUser(String name, String nickname, String email, String password, String profileImageUrl) {
        User user = User.of(name, nickname, email, password, profileImageUrl, true);

        fileStorage.save(directory, user);

        return user;
    }

    @Override
    public User findUserByIdOrThrow(UUID id) {
        return null;
    }

    @Override
    public List<User> findAllUsers() {
        return null;
    }

    @Override
    public void updateUser(UUID id, String name, String nickname, String email, String password, String profileImageUrl) {

    }

    @Override
    public void deleteUser(UUID id) {

    }
}
