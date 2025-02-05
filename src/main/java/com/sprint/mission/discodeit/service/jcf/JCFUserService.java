package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.security.Encryptor;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.util.UUID;

public class JCFUserService implements UserService {
  private final UserRepository userRepository;
  private final Encryptor encryptor;
  
  public JCFUserService(UserRepository userRepository, Encryptor encryptor) {
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
    userRepository.findUserById(id)
        .ifPresentOrElse(u -> u.updateName(newName), () -> {
          throw new IllegalArgumentException("user not found: " + id);
        });
  }
  
  @Override
  public void updateUserPassword(UUID id, String newPassword) {
    userRepository.findUserById(id)
        .ifPresentOrElse(u -> u.updatePassword(newPassword), () -> {
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
