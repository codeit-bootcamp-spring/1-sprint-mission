package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.CreateUserDto;
import com.sprint.mission.discodeit.dto.user.FindUserDto;
import com.sprint.mission.discodeit.dto.user.UpdateUserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.security.Encryptor;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validation.UserValidator;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BasicUserService implements UserService {
  @Qualifier("file")
  private final UserRepository userRepository;
  @Qualifier("file")
  private final UserStatusRepository userStatusRepository;
  private final Encryptor encryptor;
  private UserValidator userValidator;
  @Qualifier("file")
  private BinaryContentRepository binaryContentRepository;
  
  @Override
  public void createUser(CreateUserDto createUserDto) {
    if (userValidator.validateUser(createUserDto)) {
      User user = new User(createUserDto.password(), createUserDto.name(), createUserDto.email(), encryptor, createUserDto.profileImage());
      UserStatus userStatus = new UserStatus(user.getId());
      userRepository.save(user);
      userStatusRepository.save(userStatus);
    }
    throw new IllegalArgumentException("Failed to create user.");
  }
  
  @Override
  public FindUserDto findById(UUID id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("user not found with id: " + id));
    UserStatus status = new UserStatus(user.getId());
    FindUserDto userDto = new FindUserDto(user.getId(),
        user.getName(),
        user.getEmail(),
        user.getProfileImage(),
        status);
    
    return userDto;
  }
  
  @Override
  public FindUserDto findByName(String name) {
    User user = userRepository.findByName(name)
        .orElseThrow(() -> new NoSuchElementException("user not found with name: " + name));
    UserStatus status = new UserStatus(user.getId());
    FindUserDto userDto = new FindUserDto(user.getId(),
        user.getName(),
        user.getEmail(),
        user.getProfileImage(),
        status);
    
    return userDto;
  }
  
  @Override
  public List<FindUserDto> findAll() {
    List<FindUserDto> userDtos = new ArrayList<>();
    userRepository.findAll().forEach(u -> {
      UserStatus status = new UserStatus(u.getId());
      userDtos.add(new FindUserDto(u.getId(), u.getName(), u.getEmail(), u.getProfileImage(), status));
    });
    return userDtos;
  }
  
  @Override
  public void updateName(UpdateUserDto updateUserDto) {
    User user = userRepository.findById(updateUserDto.id())
        .orElseThrow(() -> new NoSuchElementException("user not found with id: " + updateUserDto.id()));
    user.updateName(updateUserDto.newName());
  }
  
  @Override
  public void updatePassword(UpdateUserDto updateUserDto) {
    User user = userRepository.findById(updateUserDto.id())
        .orElseThrow(() -> new NoSuchElementException("user not found with id: " + updateUserDto.id()));
    user.updatePassword(updateUserDto.newPassword());
  }
  
  @Override
  public void updateProfileImage(UpdateUserDto updateUserDto) {
    User user = userRepository.findById(updateUserDto.id())
        .orElseThrow(() -> new NoSuchElementException("user not found with id: " + updateUserDto.id()));
    user.updateProfileImage(updateUserDto.profileImage());
  }
  
  @Override
  public void remove(UUID id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("user not found with id: " + id));
    binaryContentRepository.remove(user.getProfileImage().get().getId());
    userStatusRepository.remove(id);
    userRepository.remove(id);
  }
  
}
