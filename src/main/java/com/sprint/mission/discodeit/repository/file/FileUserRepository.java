package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.domain.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.util.FileIOUtil.*;
import static com.sprint.mission.discodeit.util.FileIOUtil.saveToFile;

public class FileUserRepository implements UserRepository {
    private final Path filePath = Path.of("./result/users.ser");
    private final Map<UUID, User> userMap;

    public FileUserRepository() {
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
    public Optional<User> findByPhone(String phone) {
        return userMap.values().stream()
                .filter(user -> user.getPhone().equals(phone))
                .findFirst();
    }

    @Override
    public List<User> findAll() {
        return userMap.values().stream()
                .collect(Collectors.toList());
    }

    @Override
    public User delete(User user) {
        userMap.remove(user.getId());
        saveToFile(userMap, filePath);
        return user;
    }
}
