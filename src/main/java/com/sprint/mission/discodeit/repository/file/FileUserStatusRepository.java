package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileUserStatusRepository implements UserStatusRepository {
    private final String filePath;

    public FileUserStatusRepository(@Value("${file.path.userstatus}") String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void deleteById(UUID id) {
        List<UserStatus> userStatuses = load().stream()
                .filter(userStatus -> !userStatus.getId().equals(id))
                .toList();
        saveToFile(userStatuses);
    }

    @Override
    public Optional<UserStatus> findById(UUID uuid) {
        return load().stream()
                .filter(userStatus -> userStatus.getId().equals(uuid))
                .findFirst();
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        List<UserStatus> userStatuses = load();
        userStatuses.add(userStatus);
        saveToFile(userStatuses);
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return load().stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public List<UserStatus> findAll() {
        return load();
    }

    private List<UserStatus> load() {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (List<UserStatus>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("파일 읽기 실패: " + file.getAbsolutePath(), e);
        }
    }

    private void saveToFile(List<UserStatus> statuses) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(statuses);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패: " + filePath, e);
        }
    }
}
