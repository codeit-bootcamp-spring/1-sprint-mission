package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserDuplicatedEmailException;
import com.sprint.mission.discodeit.exception.user.UserDuplicatedUsernameException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundExeption;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.transaction.Transactional;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service // 서비스 Bean
@RequiredArgsConstructor // 생성자
@Slf4j
public class BasicUserService implements UserService {

  private final UserRepository userRepository;

  private final BinaryContentRepository binaryContentRepository;
  private final UserMapper userMapper;
  private final BinaryContentStorage binaryContentStorage;

  @Transactional
  @Override
  public UserDto create(UserCreateRequest userCreateRequest,
      Optional<BinaryContentCreateRequest> profileCreateRequest) {
    log.info("User 생성 시작 - username: {} and email: {}", userCreateRequest.username(),
        userCreateRequest.email());
    //username,email 중복 확인
    validateUsernameAndEmail(userCreateRequest.username(), userCreateRequest.email());

    //프로필 이미지 체크
    BinaryContent profile = profileIdCheck(profileCreateRequest);

    //User 생성
    User user = new User(
        userCreateRequest.username(),
        userCreateRequest.email(),
        userCreateRequest.password(),
        profile
    );

    Instant now = Instant.now();
    UserStatus userStatus = new UserStatus(user, now);

    userRepository.save(user);
    log.info("User 생성 성공: {}", user);

    return userMapper.toDto(user);
  }


  @Override
  public UserDto findById(UUID userId) {
    return userRepository.findById(userId)
        .map(userMapper::toDto)
        .orElseThrow(() -> new UserNotFoundExeption(
            ErrorCode.USER_NOT_FOUND,
            Map.of("userId", userId)
        ));
  }

  @Override
  public List<UserDto> findAll() {
    return userRepository.findAllWithProfileAndStatus()
        .stream()
        .map(userMapper::toDto)
        .toList();
  }

  @Transactional
  @Override
  public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
      Optional<BinaryContentCreateRequest> profileCreateRequest) {
    log.info("User 업데이트 시작: {}", userId);

    User user = userRepository.findById(userId)
        .orElseThrow(() -> {
          log.error("User ID({}) 를 찾을 수 없음", userId);
          return new UserNotFoundExeption(
              ErrorCode.USER_NOT_FOUND,
              Map.of("userId", userId)
          );
        });

    validateUsernameAndEmail(userUpdateRequest.newUsername(), userUpdateRequest.newEmail());

    BinaryContent profile = profileIdCheck(profileCreateRequest);

    user.update(
        userUpdateRequest.newUsername(),
        userUpdateRequest.newEmail(),
        userUpdateRequest.newPassword(),
        profile
    );

    log.info("User 업데이트 성공: {}", userId);

    return userMapper.toDto(user);
  }

  @Transactional
  @Override
  public void deleteById(UUID userId) {
    log.info("User 삭제 시작: {}", userId);

    //User 삭제시 binaryContent, userStatus 삭제
    if (userRepository.existsById(userId)) {
      log.error("User ID({}) 를 찾을 수 없음", userId);
      throw new UserNotFoundExeption(
          ErrorCode.USER_NOT_FOUND,
          Map.of("userId", userId)
      );
    }

    userRepository.deleteById(userId);
    log.info("User 삭제 성공: {}", userId);
  }

  //프로필 이미지 체크
  public BinaryContent profileIdCheck(Optional<BinaryContentCreateRequest> profileCreateRequest) {
    BinaryContent profile = profileCreateRequest
        .map(profileRequest -> {
          String fileName = profileRequest.fileName();
          String contentType = profileRequest.contentType();
          byte[] bytes = profileRequest.bytes();
          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType);
          binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(binaryContent.getId(), bytes);
          log.info("Binary content 저장 성공: {}", binaryContent);
          return binaryContent;
        })
        .orElse(null);
    return profile;
  }

  //username, email 다른 유저와 같은지 체크
  public void validateUsernameAndEmail(String username, String email) {
    if (userRepository.existsByEmail(email)) {
      log.error("User email {} 존재", email);
      throw new UserDuplicatedEmailException(ErrorCode.USER_DUPLICATE_EMAIL,
          Map.of("email", email));
    }
    if (userRepository.existsByUsername(username)) {
      log.error("User username {} 존재", username);
      throw new UserDuplicatedUsernameException(ErrorCode.USER_DUPLICATE_USERNAME,
          Map.of("username", username));
    }
  }
}
