package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.FileIOException;
import com.sprint.mission.discodeit.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileUserRepository implements UserRepository {

    private final Path directoryPath;
    private final String FILE_EXTENSION = ".ser";

    private final FileManager fileManager;

    public FileUserRepository(@Value("${discodeit.repository.file-directory}") String directory, FileManager fileManager) {
        this.fileManager = fileManager;
        this.directoryPath = Path.of(System.getProperty("user.dir"), directory, "users");
    }

    @PostConstruct
    private void init() {
        fileManager.createDirectory(directoryPath);
    }

    @Override
    public User save(User user) {
        Path path = directoryPath.resolve(user.getId().toString().concat(FILE_EXTENSION));

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            oos.writeObject(user);
            return user;
        } catch (IOException e) {
            throw new FileIOException("user 저장 실패");
        }
    }

    @Override
    public Optional<User> findById(UUID userId) {
        Path path = directoryPath.resolve(userId.toString().concat(FILE_EXTENSION));

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            User user = (User) ois.readObject();
            return Optional.of(user);
        } catch (IOException | ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByName(String name) {
        File[] files = directoryPath.toFile().listFiles();

        if (files == null) {
            return Optional.empty();
        }

        User user = null;
        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                User findUser = (User) ois.readObject();
                if (findUser.getName().equals(name)) {
                    user = findUser;
                    break;
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new FileIOException("users 읽기 실패");
            }
        }

        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findAll() {
        File[] files = directoryPath.toFile().listFiles();
        List<User> users = new ArrayList<>(100);

        if (files == null) {
            return users;
        }

        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                User user = (User) ois.readObject();
                users.add(user);
            } catch (IOException | ClassNotFoundException e) {
                throw new FileIOException("users 읽기 실패");
            }
        }
        return users;
    }

    @Override
    public void updateUser(User user) {
        save(user);
    }

    @Override
    public void deleteUser(UUID userId) {
        Path path = directoryPath.resolve(userId.toString().concat(FILE_EXTENSION));

        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new FileIOException("user 삭제 실패");
            }
        }
    }
}