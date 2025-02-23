package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user.FindUserDto;
import com.sprint.mission.discodeit.dto.user.UpdateUserDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.entity.security.Encryptor;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.validation.UserValidator;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class BasicUserService implements UserService {
  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final Encryptor encryptor;
  private final UserValidator userValidator;
  
  public BasicUserService(UserRepository userRepository,
                          UserStatusRepository userStatusRepository,
                          BinaryContentRepository binaryContentRepository,
                          Encryptor encryptor,
                          UserValidator userValidator) {
    this.userRepository = userRepository;
    this.userStatusRepository = userStatusRepository;
    this.binaryContentRepository = binaryContentRepository;
    this.encryptor = encryptor;
    this.userValidator = userValidator;
  }
  
  @Override
  public void createUser(UserCreateRequest userCreateRequest) {
    if (userValidator.validateUser(userCreateRequest)) {
      String salt = encryptor.getSalt();
      String encryptedPassword = encryptor.encryptPassword(userCreateRequest.password(), salt);
      User user = new User(encryptedPassword, salt, userCreateRequest.name(), userCreateRequest.email(), userCreateRequest.profileImageId());
      UserStatus userStatus = new UserStatus(user.getId());
      userRepository.save(user);
      userStatusRepository.save(userStatus);
    } else {
      throw new IllegalArgumentException("Failed to create user.");
    }
  }
  
  @Override
  public FindUserDto findById(UUID id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("user not found with id: " + id));
    UserStatus status = new UserStatus(user.getId());
    FindUserDto userDto = new FindUserDto(user.getId(),
        user.getName(),
        user.getEmail(),
        user.getProfileImageId(),
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
        user.getProfileImageId(),
        status);
    
    return userDto;
  }
  
  @Override
  public List<FindUserDto> findAll() {
    List<FindUserDto> userDtos = new ArrayList<>();
    userRepository.findAll().forEach(u -> {
      UserStatus status = new UserStatus(u.getId());
      userDtos.add(new FindUserDto(u.getId(), u.getName(), u.getEmail(), u.getProfileImageId(), status));
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
    String salt = encryptor.getSalt();
    String encryptedPassword = encryptor.encryptPassword(updateUserDto.newPassword(), salt);
    user.updatePassword(encryptedPassword, salt);
  }
  
  @Override
  public void updateProfileImage(UpdateUserDto updateUserDto) {
    User user = userRepository.findById(updateUserDto.id())
        .orElseThrow(() -> new NoSuchElementException("user not found with id: " + updateUserDto.id()));
    user.updateProfileImage(updateUserDto.profileImageId());
  }
  
  @Override
  public void remove(UUID id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new NoSuchElementException("user not found with id: " + id));
    binaryContentRepository.remove(user.getProfileImageId());
    userStatusRepository.remove(id);
    userRepository.remove(id);
  }
  
}
