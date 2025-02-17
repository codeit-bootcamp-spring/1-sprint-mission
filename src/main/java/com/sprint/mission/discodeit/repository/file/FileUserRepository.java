package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Repository
@Profile("file")
public class FileUserRepository implements UserRepository {
    private final Path directory;

    public FileUserRepository(@Value("${discodeit.repository.file-directories.users}") String directory) {
        this.directory = Paths.get(directory);
    }

    @Override
    public User save(User user) {
        Path filePath = directory.resolve(user.getId() + ".ser");
        try (
                FileOutputStream fos = new FileOutputStream(filePath.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos);
        ) {
            oos.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public User find(UUID userId) {
        Path filePath = directory.resolve(userId + ".ser");
        try (
                FileInputStream fis = new FileInputStream(filePath.toFile());
                ObjectInputStream ois = new ObjectInputStream(fis);
        ) {
            Object data = ois.readObject();
            return (User) data;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User findByName(String name) {
        try {
            return Files.list(directory)
                    .map(path -> {
                        try (
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis)
                        ) {
                            return (User) ois.readObject();
                        } catch (IOException | ClassNotFoundException e) {
                            return null;
                        }
                    }).filter(Objects::nonNull)
                    .filter(user -> user.isSameName(name))
                    .findFirst()
                    .orElse(null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> findAll() {
        try {
            List<User> users = Files.list(directory)
                    .map(path -> {
                        try (
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis);
                        ) {
                            Object data = ois.readObject();
                            return (User) data;
                        } catch (IOException | ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();
            return users;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(UUID userId) {
        Path filePath = directory.resolve(userId + ".ser");

        try {
            Files.delete(filePath);
        } catch (IOException e) {
            System.out.println("삭제에 실패하였습니다.");
        }
    }

    @Override
    public boolean existsById(UUID userId) {
        Path filePath = directory.resolve(userId + ".ser");
        return Files.exists(filePath);
    }
}
