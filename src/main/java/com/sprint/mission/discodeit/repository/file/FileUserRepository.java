package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class FileUserRepository implements UserRepository {
    private final Path directory;
    private final AtomicLong idGenerator = new AtomicLong(1);

    public FileUserRepository(@Qualifier("fileUserStoragePath") Path directory) {
        this.directory = directory;
    }

    @PostConstruct  // 의존성 주입이 완료된 후 실행
    private void init() {
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
                System.out.println("[User] User storage directory created: " + directory.toAbsolutePath());
            } catch (IOException e) {
                throw new CustomException(ErrorCode.FAILED_TO_CREATE_DIRECTORY);
            }
        }
    }

    @Override
    public Long saveUser(User user) {
        Long id = idGenerator.getAndIncrement();
        Path filePath = directory.resolve(id + ".ser");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
            oos.writeObject(user);
            return id;
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FAILED_TO_SAVE_DATA);
        }
    }

    @Override
    public User loadUser(Long id) {
        Path filePath = directory.resolve(id + ".ser");
        if (!Files.exists(filePath)) {
            throw new CustomException(ErrorCode.USER_NOT_FOUND);
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath.toFile()))) {
            return (User) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new CustomException(ErrorCode.FAILED_TO_LOAD_DATA);
        }
    }

    @Override
    public Map<Long, User> loadAllUsers() {
        try {
            return Files.list(directory)
                    .filter(Files::isRegularFile)
                    .map(path -> {
                        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
                            User user = (User) ois.readObject();
                            Long id = Long.valueOf(path.getFileName().toString().replace(".ser", ""));
                            return Map.entry(id, user);
                        } catch (IOException | ClassNotFoundException e) {
                            throw new CustomException(ErrorCode.FAILED_TO_LOAD_DATA);
                        }
                    })
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUser(Long id) {
        try {
            Files.deleteIfExists(directory.resolve(id + ".ser"));
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FAILED_TO_DELETE_DATA);
        }
    }

    @Override
    public void updateUser(Long id, User user) {
        Path filePath = directory.resolve(id + ".ser");
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath.toFile()))) {
            oos.writeObject(user);
        } catch (IOException e) {
            throw new CustomException(ErrorCode.FAILED_TO_UPDATE_DATA);
        }
    }
}
