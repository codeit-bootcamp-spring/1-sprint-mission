package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.dto.user.CreateUserDto;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserValidator {
  @Qualifier("jcf")
  private final UserRepository userRepository;
  
  private boolean isUniqueName(String name) {
    return userRepository.findAll().stream()
        .anyMatch(u -> u.getName().equals(name));
  }
  
  private boolean isValidEmail(String email) {
    return email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");
  }
  
  private boolean isUniqueEmail(String email) {
    return userRepository.findAll().stream()
        .anyMatch(u -> u.getEmail().equals(email));
  }
  
  public boolean validateUser(CreateUserDto userDto) {
    if (!isUniqueName(userDto.name())) {
      throw new IllegalArgumentException("This name already exists. name: " + userDto.name());
    }
    if (!isValidEmail(userDto.email())) {
      throw new IllegalArgumentException("Invalid email foramt: " + userDto.email());
    }
    if (!isUniqueEmail(userDto.email())) {
      throw new IllegalArgumentException("This email already exists. email: " + userDto.email());
    }
    return true;
  }
}
