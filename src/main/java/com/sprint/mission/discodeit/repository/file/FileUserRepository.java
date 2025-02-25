package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static com.sprint.mission.discodeit.util.FileIOUtil.*;
import static com.sprint.mission.discodeit.util.FileIOUtil.saveToFile;

@Profile("file")
@Repository
public class FileUserRepository implements UserRepository {
    private final Path filePath;
    private final Map<UUID, User> userMap;

    public FileUserRepository(@Value("${discodeit.repository.user-file-path}") Path filePath) {
        this.filePath = filePath;
        if (!Files.exists(this.filePath)) {
            try {
                Files.createFile(this.filePath);
                saveToFile(new HashMap<>(), this.filePath);
            } catch (IOException e) {
                throw new RuntimeException("회원 파일을 초기화 하던 중에 문제가 발생했습니다", e);
            }
        }
        this.userMap = loadFromFile(this.filePath);
    }

    @Override
    public User save(User user) {
        userMap.put(user.getId(), user);
        saveToFile(userMap, filePath);
        return user;
    }

    @Override
    public boolean userExistById(UUID userID) {
        return userMap.containsKey(userID);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userMap.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public Optional<User> findByName(String name) {
        return userMap.values().stream()
                .filter(user -> user.getName().equals(name))
                .findFirst();
    }

    @Override
    public Optional<User> findById(UUID userId) {
        return Optional.ofNullable(userMap.get(userId));
    }

    @Override
    public List<User> findAll() {
        return userMap.values().stream().toList();
    }

    @Override
    public List<User> findAllById(List<UUID> userIds) {
        return userIds.stream()
                .map(userMap::get)
                .filter(Objects::nonNull)
                .toList();
    }

    @Override
    public void delete(UUID id) {
        userMap.remove(id);
        saveToFile(userMap, filePath);
    }
}
