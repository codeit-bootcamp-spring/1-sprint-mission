package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserService implements UserService {
  private final UserRepository userRepository;

  public JCFUserService(UserRepository userRepository){
    this.userRepository = userRepository;
  }

  @Override
  public void createUser(User user) {
    userRepository.save(user);
  }

  @Override
  public Optional<User> readUser(UUID id) {
    return userRepository.findById(id);
  }

  @Override
  public List<User> readAllUsers() {
    return userRepository.findAll();
  }

  @Override
  public void updateUser(User user, String name, int age, char gender) {
    userRepository.updateOne(user, name, age ,gender);
  }

  @Override
  public void deleteUser(UUID id) {
    userRepository.deleteOne(id);
  }
}