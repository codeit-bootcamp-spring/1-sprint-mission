package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Repository
public class FileUserRepository implements UserRepository {

  private final Path DIRECTORY;
  private final Path FILE_PATH;
  private final Map<UUID, User> data;

  public FileUserRepository(
      @Value("${discodeit.repository.file.storage-path}") String storagePath) {
    this.DIRECTORY = Paths.get(System.getProperty("user.dir"), storagePath, "User");
    this.FILE_PATH = DIRECTORY.resolve("user.ser");

    if (Files.notExists(DIRECTORY)) {
      try {
        Files.createDirectories(DIRECTORY);
      } catch (IOException e) {
        throw new RuntimeException("Failed to create directory: " + DIRECTORY, e);
      }
    }
    this.data = loadDataFromFile();
  }


  @Override
  public User save(User user) {
    data.put(user.getId(), user);
    saveDataToFile();
    return user;
  }

  @Override
  public Optional<User> getUserById(UUID id) {
    return Optional.ofNullable(this.data.get(id));
  }

  @Override
  public List<User> getAllUsers() {
    return this.data.values().stream().toList();
  }

  @Override
  public boolean existsById(UUID id) {
    return this.data.containsKey(id);
  }


  @Override
  public void deleteUser(UUID id) {
    if (!this.data.containsKey(id)) {
      throw new NoSuchElementException("User with id" + id + " not found");
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

  @Override
  public Optional<User> findByUsername(String username) {
    return this.getAllUsers().stream()
        .filter(user -> user.getUsername().equals(username))
        .findFirst();
  }

  // 데이터를 파일에 저장
  private void saveDataToFile() {
    try (ObjectOutputStream oos = new ObjectOutputStream(
        new FileOutputStream(FILE_PATH.toFile()))) {
      oos.writeObject(data);
    } catch (IOException e) {
      throw new RuntimeException("Failed to save data to file: " + e.getMessage(), e);
    }
  }

  // 파일에서 데이터를 로드
  private Map<UUID, User> loadDataFromFile() {
    File file = FILE_PATH.toFile();
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
