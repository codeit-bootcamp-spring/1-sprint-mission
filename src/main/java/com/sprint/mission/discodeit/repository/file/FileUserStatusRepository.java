package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.domain.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.sprint.mission.discodeit.util.FileIOUtil.loadFromFile;
import static com.sprint.mission.discodeit.util.FileIOUtil.saveToFile;

@Profile("file")
@Repository
public class FileUserStatusRepository implements UserStatusRepository {
    private final Path filePath;
    private final Map<UUID, UserStatus> userStatusMap;

    // 생성자에서 Path를 입력받도록 변경
    public FileUserStatusRepository(@Value("${discodeit.repository.user-status-file-path}") Path filePath) {
        this.filePath = filePath;

        if (!Files.exists(this.filePath)) {
            try {
                Files.createFile(this.filePath);
                saveToFile(new HashMap<>(), this.filePath);
            } catch (IOException e) {
                throw new RuntimeException("회원 파일을 초기화 하던 중에 문제가 발생했습니다", e);
            }
        }

        this.userStatusMap = loadFromFile(this.filePath);
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        userStatusMap.put(userStatus.getId(), userStatus);
        saveToFile(userStatusMap, filePath);
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userID) {
        return userStatusMap.values().stream()
                .filter(userStatus -> userStatus.getUserId().equals(userID))
                .findFirst();
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        return userStatusMap.values().stream()
                .filter(userStatus -> userStatus.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<UserStatus> findAll() {
        return userStatusMap.values().stream().toList();
    }

    @Override
    public void deleteById(UUID id) {
        userStatusMap.remove(id);
        saveToFile(userStatusMap, filePath);
    }

    @Override
    public void deleteByUserId(UUID userID) {
        List<UUID> keysToRemove = userStatusMap.entrySet().stream()
                .filter(entry -> entry.getValue().getUserId().equals(userID))
                .map(Map.Entry::getKey)
                .toList();

        keysToRemove.forEach(userStatusMap::remove);
    }
}
