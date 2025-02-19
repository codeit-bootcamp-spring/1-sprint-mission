package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileUserStatusRepository implements UserStatusRepository {
    private final Path DIRECTORY;
    private final String EXTENSION = ".dat";

    public FileUserStatusRepository() {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), "file-data-map", UserStatus.class.getSimpleName());
        if (Files.notExists(DIRECTORY)) {
            try {
                Files.createDirectories(DIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private Path resolvePath(UUID id) {
        return DIRECTORY.resolve(id + EXTENSION);
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        Path path = resolvePath(userStatus.getUser().getId());
        try (FileOutputStream fos = new FileOutputStream(path.toFile());
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(userStatus);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findByUser(User user) {
        return findById(user.getId());
    }

    @Override
    public void deleteByUser(User user) {
        deleteById(user.getId());
    }

    @Override
    public boolean existsByUser(User user) {
        return existsById(user.getId());
    }

    @Override
    public List<UserStatus> findAllByUserIn(List<User> users) {
        List<UserStatus> result = new ArrayList<>();
        for (User user : users) {
            findByUser(user).ifPresent(result::add);
        }
        return result;
    }

    private Optional<UserStatus> findById(UUID id) {
        Path path = resolvePath(id);
        if (Files.exists(path)) {
            try (FileInputStream fis = new FileInputStream(path.toFile());
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                return Optional.of((UserStatus) ois.readObject());
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return Optional.empty();
    }

    private boolean existsById(UUID id) {
        return Files.exists(resolvePath(id));
    }

    private void deleteById(UUID id) {
        Path path = resolvePath(id);
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
