package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.FileIOException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Primary
@Repository
public class FileUserStatusRepository implements UserStatusRepository {

    private final String directory = "user_statuses";
    private final String FILE_EXTENSION = ".ser";

    private final FileManager fileManager = new FileManager(directory);
    private final Path filePath = fileManager.getPath();

    @Override
    public UserStatus save(UserStatus userStatus) {
        Path path = filePath.resolve(userStatus.getUserId().toString().concat(FILE_EXTENSION));

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(path.toFile()))) {
            oos.writeObject(userStatus);
            return userStatus;
        } catch (IOException e) {
            throw new FileIOException("UserStatus 저장 실패");
        }
    }

    @Override
    public UserStatus findById(UUID userId) {
        Path path = filePath.resolve(userId.toString().concat(FILE_EXTENSION));

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(path.toFile()))) {
            return (UserStatus)ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new NotFoundException("등록되지 않은 UserStatus입니다.");
        }
    }

    @Override
    public List<UserStatus> findAll() {
        File[] files = filePath.toFile().listFiles();
        List<UserStatus> userStatuses = new ArrayList<>(100);

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
        Path path = filePath.resolve(userId.toString().concat(FILE_EXTENSION));

        if (Files.exists(path)) {
            try {
                Files.delete(path);
            } catch (IOException e) {
                throw new FileIOException("UserStatus 삭제 실패");
            }
        }
    }
}
