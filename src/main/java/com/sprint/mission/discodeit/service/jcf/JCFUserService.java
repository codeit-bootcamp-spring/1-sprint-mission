package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.Gender;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.repository.UserRepository;

import java.util.*;

public class JCFUserService implements UserService {
  // 1. 의존성 상수 선언 2. 생성자
  private final UserRepository userRepository;
  public JCFUserService(UserRepository userRepository){
    this.userRepository = userRepository;
  }

  @Override
  public void createUser(User user) {
    data.add(user);
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
  public void updateUser(User user, String name, int age, Gender gender) {
    userRepository.updateOne(user, name, age ,gender);
  }

  @Override
  public void deleteUser(UUID id) {
    userRepository.deleteOne(id);
  }
}