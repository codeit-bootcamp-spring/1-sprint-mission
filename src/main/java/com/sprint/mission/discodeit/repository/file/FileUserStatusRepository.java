package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.FileIOException;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
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
public class FileUserStatusRepository implements UserStatusRepository {

    private final Path directoryPath;
    private final String FILE_EXTENSION = ".ser";

    private final FileManager fileManager;

    public FileUserStatusRepository(@Value("${discodeit.repository.file-directory}") String directory, FileManager fileManager) {
        this.fileManager = fileManager;
        this.directoryPath = Path.of(System.getProperty("user.dir"), directory, "user_statuses");
    }

    @PostConstruct
    private void init() {
        fileManager.createDirectory(directoryPath);
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        Path path = directoryPath.resolve(userStatus.getUserId().toString().concat(FILE_EXTENSION));

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            oos.writeObject(userStatus);
            return userStatus;
        } catch (IOException e) {
            throw new FileIOException("UserStatus 저장 실패");
        }
    }

    @Override
    public Optional<UserStatus> findById(UUID userId) {
        Path path = directoryPath.resolve(userId.toString().concat(FILE_EXTENSION));

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            UserStatus userStatus = (UserStatus) ois.readObject();
            return Optional.of(userStatus);
        } catch (IOException | ClassNotFoundException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<UserStatus> findAll() {
        File[] files = directoryPath.toFile().listFiles();
        List<UserStatus> userStatuses = new ArrayList<>(100);

        if (files == null) {
            return userStatuses;
        }

        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                UserStatus userStatus = (UserStatus) ois.readObject();
                userStatuses.add(userStatus);
            } catch (IOException | ClassNotFoundException e) {
                throw new FileIOException("UserStatus 읽기 실패");
            }
        }
        return userStatuses;
    }

    @Override
    public void delete(UUID userId) {
        Path path = directoryPath.resolve(userId.toString().concat(FILE_EXTENSION));

        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new FileIOException("UserStatus 삭제 실패");
            }
        }
    }
}
