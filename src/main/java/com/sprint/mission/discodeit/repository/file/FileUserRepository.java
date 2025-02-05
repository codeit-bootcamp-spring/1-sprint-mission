package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FileUserRepository implements UserRepository {
    private static final String FILE_PATH = "tmp/user.ser";
    private final FileManager<User> fileManager =  new FileManager<>(FILE_PATH);

    @Override
    public User save(User user) {
        Map<UUID, User> savedUserMap = loadUserMapToFile();
        savedUserMap.put(user.getId(), user);
        saveUserMapToFile(savedUserMap);
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        Map<UUID, User> savedUserMap = loadUserMapToFile();
        return Optional.ofNullable(savedUserMap.get(id));
    }

    @Override
    public List<User> findAll() {
        return fileManager.loadListToFile();
    }

    @Override
    public void deleteById(UUID id) {
        Map<UUID, User> savedUserMap = loadUserMapToFile();
        savedUserMap.remove(id);
        saveUserMapToFile(savedUserMap);
    }

    private void saveUserMapToFile(Map<UUID, User> saveUserMap) {
        List<User> saveUserList = saveUserMap.values().stream().collect(Collectors.toList());
        fileManager.saveListToFile(saveUserList);
    }

    private Map<UUID, User> loadUserMapToFile() {
        List<User> loadUserList = fileManager.loadListToFile();
        if (loadUserList.isEmpty()) {
            return new HashMap<>();
        }
        return loadUserList.stream()
                .collect(Collectors.toMap(User::getId, Function.identity()));
    }
}
