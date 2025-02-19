package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileUserStatusRepository implements UserStatusRepository {
    private static final String FILE_PATH = "user_status.ser";
    private final FileManager<UserStatus> fileManager =  new FileManager<>(FILE_PATH);

    @Override
    public UserStatus save(UserStatus userStatus) {
        Map<UUID, UserStatus> savedUserStatusMap = loadUserStatusMapToFile();
        savedUserStatusMap.put(userStatus.getId(), userStatus);
        saveUserStatusMapToFile(savedUserStatusMap);
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        return Optional.ofNullable(loadUserStatusMapToFile().get(id));
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return fileManager.loadListToFile().stream()
                .filter(userStatus -> userStatus.getUserId().equals(userId))
                .findAny();
    }

    @Override
    public List<UserStatus> findAll() {
        return fileManager.loadListToFile();
    }

    @Override
    public void deleteById(UUID id) {
        Map<UUID, UserStatus> savedUserStatusMap = loadUserStatusMapToFile();
        savedUserStatusMap.remove(id);
        saveUserStatusMapToFile(savedUserStatusMap);
    }

    @Override
    public void deleteByUserId(UUID userId) {
        Map<UUID, UserStatus> savedUserStatusMap = loadUserStatusMapToFile();
        for (UUID id : savedUserStatusMap.keySet()) {
            if (savedUserStatusMap.get(id).getUserId().equals(userId)) {
                savedUserStatusMap.remove(id);
                break;
            }
        }
        saveUserStatusMapToFile(savedUserStatusMap);
    }

    @Override
    public boolean existsByUserId(UUID userId) {
        List<UserStatus> savedUserStatusList = fileManager.loadListToFile();
        if (savedUserStatusList.isEmpty()) {
            return false;
        }
        return fileManager.loadListToFile().stream()
                .anyMatch(userStatus -> userStatus.getUserId().equals(userId));
    }

    private void saveUserStatusMapToFile(Map<UUID, UserStatus> saveUserStatusMap) {
        List<UserStatus> saveUserStatusList = saveUserStatusMap.values().stream().collect(Collectors.toList());
        fileManager.saveListToFile(saveUserStatusList);
    }

    private Map<UUID, UserStatus> loadUserStatusMapToFile() {
        List<UserStatus> loadUserStatusList = fileManager.loadListToFile();
        if (loadUserStatusList.isEmpty()) {
            return new HashMap<>();
        }
        return loadUserStatusList.stream()
                .collect(Collectors.toMap(UserStatus::getId, Function.identity()));
    }
}
