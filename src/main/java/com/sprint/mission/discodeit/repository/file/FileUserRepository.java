package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.stereotype.Repository;


import java.io.*;
import java.util.*;

//@Repository
public class FileUserRepository implements UserRepository {
    private final String FILE_PATH="user.ser";
    private final Map<UUID, User> data;

    public FileUserRepository() {
        this.data=loadDataFromFile();
    }


    @Override
    public User save(User user) {
        data.put(user.getId(),user);
        saveDataToFile();
        return user;
    }

    @Override
    public Optional<User> getUserById(UUID id) {
        return Optional.ofNullable(this.data.get(id));
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(data.values());
    }

    @Override
    public boolean existsById(UUID id) {
        return this.data.containsKey(id);
    }


    @Override
    public void deleteUser(UUID id) {
        if(!this.data.containsKey(id)){
            throw new NoSuchElementException("Channel with id"+id+" not found");
        }
        data.remove(id);
        saveDataToFile();
    }

    @Override
    public boolean existsByEmail(String email) {
        return this.data.containsKey(email);
    }

    @Override
    public boolean existsByName(String name) {
        return this.data.containsKey(name);
    }


    // 데이터를 파일에 저장
    private void saveDataToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save data to file: " + e.getMessage(), e);
        }
    }

    // 파일에서 데이터를 로드
    private Map<UUID, User> loadDataFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new HashMap<>();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (Map<UUID, User>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to load data from file: " + e.getMessage(), e);
        }
    }
}
