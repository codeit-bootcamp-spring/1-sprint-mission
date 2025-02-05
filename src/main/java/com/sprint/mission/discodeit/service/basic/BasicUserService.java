package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.security.Encryptor;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicUserService implements UserService {
  private final UserRepository userRepository;
  private final Encryptor encryptor;
  
  public BasicUserService(UserRepository userRepository, Encryptor encryptor) {
    this.userRepository = userRepository;
    this.encryptor = encryptor;
  }
  
  @Override
  public void createUser(String password, String name) {
    User user = new User(password, name, encryptor);
    userRepository.save(user);
  }
  
  @Override
  public void updateUserName(UUID id, String newName) {
    User user = userRepository.findUserById(id)
        .orElseThrow(() -> new NoSuchElementException("user not found: " + id));
    user.updateName(newName);
  }
  
  @Override
  public void updateUserPassword(UUID id, String newPassword) {
    User user = userRepository.findUserById(id)
        .orElseThrow(() -> new NoSuchElementException("user not found: " + id));
    user.updatePassword(newPassword);
  }
  
  @Override
  public void removeUser(UUID id) {
    userRepository.remove(id);
  }
  
  @Override
  public User findUserByName(String name) {
    return userRepository.findUserByName(name)
        .orElseThrow(() -> new NoSuchElementException("user not found: " + name));
  }
}
