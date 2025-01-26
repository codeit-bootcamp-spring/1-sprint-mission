package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.security.Encryptor;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class FileUserService implements UserService {
  private final FileUserRepository userRepository;
  private final Encryptor encryptor;
  private List<User> users = new ArrayList<>();
  
  public FileUserService(FileUserRepository userRepository, Encryptor encryptor) {
    this.userRepository = userRepository;
    this.encryptor = encryptor;
  }
  
  private void loadAllUsers() {
    users = userRepository.findAllUsers();
  }
  
  private void saveUserToCsv(User user) throws FileNotFoundException {
    userRepository.save(user);
  }
  
  @Override
  public void createUser(String password, String name) {
    User newUser = new User(password, name, encryptor);
    try {
      saveUserToCsv(newUser);
    } catch (FileNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
  
  @Override
  public void updateUserName(UUID id, String newName) {
    Optional<User> user = userRepository.findUserById(id);
    user.ifPresentOrElse(u -> {
      u.updateName(newName);
      userRepository.save(u);
    }, () -> {
      throw new IllegalArgumentException("user not found: " + id);
    });
  }
  
  @Override
  public void updateUserPassword(UUID id, String newPassword) {
    Optional<User> user = userRepository.findUserById(id);
    user.ifPresentOrElse(u -> {
      u.updatePassword(newPassword);
      userRepository.save(u);
    }, () -> {
      throw new IllegalArgumentException("user not found: " + id);
    });
  }
  
  @Override
  public void removeUser(UUID id) {
    userRepository.remove(id);
    
  }
  
  @Override
  public User findUserByName(String name) {
    return userRepository.findUserByName(name)
        .orElseThrow(() -> new IllegalArgumentException("user not found: " + name));
  }
  
}
