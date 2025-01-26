package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.util.FileIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileUserRepository implements UserRepository {
  private static final File file = new File("resources/users.ser");
  private FileIO fileIO;
  private List<User> users = new ArrayList<>();
  
  private void loadAllUsers() {
    fileIO.initFile(file);
    List<Serializable> serializables = fileIO.loadFile(file);
    users = serializables.stream()
        .filter(User.class::isInstance)
        .map(User.class::cast)
        .toList();
  }
  
  @Override
  public Optional<User> findUserById(UUID id) {
    return users.stream().filter(u -> u.getId().equals(id)).findFirst();
  }
  
  @Override
  public Optional<User> findUserByName(String name) {
    return users.stream().filter(u -> u.getName().equals(name)).findFirst();
  }
  
  @Override
  public List<User> findAllUsers() {
    return users.stream().toList();
  }
  
  @Override
  public void save(User user) {
    try {
      fileIO.saveToFile(file, user);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
  
  @Override
  public void remove(UUID id) {
    User user = findUserById(id).orElse(null);
    fileIO.removeObjectFromFile(file, user);
  }
}
