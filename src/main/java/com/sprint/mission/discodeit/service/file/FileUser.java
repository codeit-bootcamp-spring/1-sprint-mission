package com.sprint.mission.discodeit.service.file;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.file.repository.FileUserRepository;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;


public class FileUser implements FileUserRepository {
  private static final String FILE_PATH = "src/main/java/com/sprint/mission/discodeit/service/file/data/user/user.json";
  private static final ObjectMapper objectMapper = new ObjectMapper();

  private Set<String> nameCheck() {

    File file = new File("src/main/java/com/sprint/mission/discodeit/service/file/data/user/user.json");
    if (!file.exists()) return new HashSet<>();
    try {
      return objectMapper.readValue(file, new TypeReference<Set<String>>() {});
    } catch (IOException e) {
      e.printStackTrace();
      return new HashSet<>() {
      };
    }
  }

  private List<User> readAllUsers() {
    File file = new File(FILE_PATH);
    if (!file.exists()) return new ArrayList<>();
    try {
      return objectMapper.readValue(file, new TypeReference<List<User>>() {});
    } catch (IOException e) {
      e.printStackTrace();
      return new ArrayList<>();
    }
  }

  @Override
  public void creat(String name){
    System.out.println(System.getProperty("user.dir"));
    User user = new User(name);
    try {
      List<User> users = readAllUsers();
      users.add(user);
      saveToFile(users);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  @Override
  public void delete(UUID userId) {
    try {
      List<User> users = readAllUsers();
      users.removeIf(user -> user.getId().equals(userId));
      saveToFile(users);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void update(UUID userId, String name) {
    List<User> userList = readAllUsers();

    boolean updated = userList.stream()
        .filter(user -> user.getId().equals(userId))
        .findFirst()
        .map(user -> {
          user.updateName(name);
          return true;
        })
        .orElse(false);

    if (updated) {
      saveToFile(userList);
    } else {
      throw new IllegalArgumentException("User with ID not found.");
    }
  }

  private void saveToFile(List<User> users) {
    try {
      objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(FILE_PATH), users);
    }
    catch (Exception e) {
      System.out.println(e);
    }
  }

  @Override
  public UUID findByName(String name) {
    List<User> userList = readAllUsers();
    Optional<User> user = userList.stream().filter(user1 -> user1.getName().equals(name)).findFirst();
    if(user.isPresent()) {
      return user.get().getId();
    }
   else {
      throw new IllegalArgumentException("There is no user with that name.");
    }
  }

  @Override
  public List<User> findByAll() {
    List<User> userList = readAllUsers();
    return userList.stream()
        .map(User::new)
        .collect(Collectors.toList());
  }

}
