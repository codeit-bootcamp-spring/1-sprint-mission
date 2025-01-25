package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.FileStorage;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class FileUserService implements UserService {
    private static final Path ROOT_DIR = Paths.get(System.getProperty("user.dir"), "tmp");
    private static final String USER_FILE = "user.ser";
    private FileStorage<User> fileStorage;

    public FileUserService() {
        this.fileStorage = new SerializableFileStorage<>(User.class);
        fileStorage.init(ROOT_DIR);
    }

    @Override
    public User createUser(User user) {
        List<User> users = readAllUsers();
        if(users.stream().anyMatch(u->u.getId().equals(user.getId()))) {
            throw new IllegalArgumentException("User Id is already exists: " + user.getId());
        }
        users.add(user);
        fileStorage.save(ROOT_DIR.resolve(USER_FILE), users);
        System.out.println(user);
        return user;
    }

    @Override
    public Optional<User> readUser(UUID existUserId) {
        return readAllUsers().stream()
                .filter(u -> u.getId().equals(existUserId))
                .findFirst();
    }

    @Override
    public List<User> readAllUsers() {
        return fileStorage.load(ROOT_DIR);
    }

    @Override
    public User updateUser(UUID existUserId, User updateUser) {
        List<User> users = readAllUsers();
        Optional<User> userToUpdate = users.stream()
                .filter(u -> u.getId().equals(existUserId))
                .findFirst();

        if (userToUpdate.isEmpty()) {
            throw new NoSuchElementException("User not found: " + existUserId);
        }

        User user = userToUpdate.get();
        System.out.println("기존 유저= " + user);
        user.updateTime();
        user.updateUserid(updateUser.getUserid());
        user.updateUsername(updateUser.getUsername());
        user.updatePassword(updateUser.getPassword());
        user.updateUserEmail(updateUser.getEmail());

        fileStorage.save(ROOT_DIR.resolve(USER_FILE), users);

        System.out.println("수정 유저= " + user);
        return user;
    }

    @Override
    public boolean deleteUser(UUID userId) {
        List<User> users = readAllUsers();
        boolean removed = users.removeIf(u -> u.getId().equals(userId));

        if (removed) {
            fileStorage.save(ROOT_DIR.resolve(USER_FILE), users);
            System.out.println("Deleted user: " + userId);
        }
        return removed;
    }
}
