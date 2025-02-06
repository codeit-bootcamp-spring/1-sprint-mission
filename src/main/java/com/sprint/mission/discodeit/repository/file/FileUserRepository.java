package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.FileIOException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class FileUserRepository implements UserRepository {

    private static final Path filePath;
    private final String FILE_EXTENSION = ".ser";

    static {
        filePath = Paths.get(System.getProperty("user.dir"), "users");
        FileManager.createDirectory(filePath);
    }

    @Override
    public User save(User user) {
        Path path = filePath.resolve(user.getId().toString().concat(FILE_EXTENSION));

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            oos.writeObject(user);
            return user;
        } catch (IOException e) {
            throw new FileIOException("user 저장 실패");
        }
    }

    @Override
    public User findById(UUID userId) {
        Path path = filePath.resolve(userId.toString().concat(FILE_EXTENSION));

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            return (User)ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new NotFoundException("등록되지 않은 user입니다.");
        }
    }

    @Override
    public List<User> findAll() {
        try {
            return Files.list(filePath)
                    .map(path -> {
                        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
                            Object data = ois.readObject();
                            return (User) data;
                        } catch (IOException | ClassNotFoundException e) {
                            throw new FileIOException("users 읽기 실패");
                        }
                    })
                    .toList();
        } catch (IOException e) {
            throw new FileIOException("users 읽기 실패");
        }
    }

    @Override
    public void updateUser(User user) {
        save(user);
    }

    @Override
    public void deleteUser(UUID userId) {
        Path path = filePath.resolve(userId.toString().concat(FILE_EXTENSION));

        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new FileIOException("user 삭제 실패");
            }
        }
    }
}