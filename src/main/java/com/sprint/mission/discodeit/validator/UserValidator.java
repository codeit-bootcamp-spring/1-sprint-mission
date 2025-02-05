package com.sprint.mission.discodeit.validator;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserValidator {
  private final UserRepository userRepository;
  public void throwIfNoSuchUserId(String userId) {
    if (userRepository.findById(userId).isEmpty()) {
      throw new UserNotFoundException();
    }
  }

  public void throwIfNoSuchUsername(String username){
    if(userRepository.findAll().stream().filter(user -> user.getUsername().equals(username)).findAny().isEmpty()){
      throw new UserNotFoundException();
    }
  }
}
