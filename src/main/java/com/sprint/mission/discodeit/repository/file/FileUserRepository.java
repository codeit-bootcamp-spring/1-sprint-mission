package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
//@Primary
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
        return Optional.ofNullable(loadUserMapToFile().get(id));
    }

    @Override
    public Optional<User> findByName(String name) {
        return loadUserMapToFile().values().stream()
                .filter(user -> user.getName().equals(name)).findAny();
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

    @Override
    public boolean existsById(UUID id) {
        return loadUserMapToFile().containsKey(id);
    }

    @Override
    public boolean existsByName(String name) {
        return loadUserMapToFile().values().stream()
                .anyMatch(user -> user.getName().equals(name));
    }

    @Override
    public boolean existsByEmail(String email) {
        return loadUserMapToFile().values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
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
