package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentRequest;
import com.sprint.mission.discodeit.dto.user.UserRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDTO;
import com.sprint.mission.discodeit.dto.userstatus.UserStatusUpdateDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor //final 혹은 @NotNull이 붙은 필드의 생성자를 자동 생성하는 롬복 어노테이션
@Slf4j

public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusService userStatusService;

  private final UserMapper userMapper;

  private final BinaryContentStorage binaryContentStorage;
  private final BinaryContentRepository binaryContentRepository;

  @Transactional
  @Override
  public UserDto createUser(UserRequest userRequest,
      Optional<BinaryContentRequest> optionalProfileCreateRequest) {
    if (userRepository.existsByUsername(userRequest.name())) {
      log.info("Username already exists : {}", userRequest.name());
      throw new IllegalArgumentException("이미 존재하는 이름입니다. ");
    }
    if (userRepository.existsByEmail(userRequest.email())) {
      log.info("Email already exists : {}", userRequest.email());
      throw new IllegalArgumentException("이미 존재하는 이메일입니다. ");
    }

    //nullable한 프로필
    BinaryContent nullableProfile = optionalProfileCreateRequest
        .map(profileRequest -> {
          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();
          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType);
          binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(binaryContent.getId(), bytes);
          log.info("Profile created with ID : {} ", binaryContent.getId());
          return binaryContent;
        })
        .orElse(null);

    User user = User.builder()
        .username(userRequest.name())
        .email(userRequest.email())
        .password(userRequest.password())
        .profile(nullableProfile)
        .build();

    log.debug("DEBUG: User created : {}", user); //debug 로그에는 엔티티를 모두 노출해도 될까?
    log.info("User created successfully with ID: {}", user.getId());
    return userMapper.toDto(userRepository.save(user));
  }

  @Override
  public UserDto findUserDTO(UUID userId) {
    User user = userRepository.findById(userId).orElseThrow(
        () -> new NoSuchElementException("user Not found"));
    return userMapper.toDto(user);
  }

  //내부 사용전용
  private User findbyId(UUID userId) {
    return userRepository.findById(userId).orElseThrow(
        () -> new NoSuchElementException("user Not found")
    );
  }

  private List<User> findAll() {
    return userRepository.findAll();
  }

  @Override
  public List<UserDto> findAllUserDTO() {
    List<User> userList = findAll();

    return userList.stream()
        .map(user -> userMapper.toDto(user))
        .collect(Collectors.toList());
  }

  @Transactional
  @Override
  public UserDto updateUser(
      UUID userID, UserUpdateDTO userUpdateDTO,
      Optional<BinaryContentRequest> optionalProfileCreateRequest) {

    //nullable한 프로필
    BinaryContent nullableProfile = optionalProfileCreateRequest
        .map(profileRequest -> {
          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();
          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType);
          binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(binaryContent.getId(), bytes);
          log.info("Profile image created with ID : {} ", binaryContent.getId());
          return binaryContent;
        })
        .orElse(null);

    User user = findbyId(userID);

    user.updateUser(userUpdateDTO.newName(), userUpdateDTO.newEmail(), userUpdateDTO.newPassword(),
        nullableProfile);
    log.debug("DEBUG: User updated : {}", user); //debug 로그에는 엔티티를 모두 노출해도 될까?
    log.info("User update successfully with ID : {} ", user.getId());
    return userMapper.toDto(userRepository.save(user));
  }

  @Override
  public void deleteUser(UUID userID) {
    userRepository.deleteById(userID);
    log.info("User deleted successfully with ID: {}", userID);
  }

  @Override
  public UserStatusUpdateDTO updateUserStatus(UUID id,
      UserStatusUpdateDTO userUserStatusUpdateDTO) {
    UserStatusUpdateDTO userStatusUpdateDTO = new UserStatusUpdateDTO(
        userUserStatusUpdateDTO.time());
    userStatusService.updateByUserId(id, userStatusUpdateDTO);
    return userUserStatusUpdateDTO;
  }

}
