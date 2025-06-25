package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.ProfileUploadDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.EmailAlreadyExistsException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.user.UsernameAlreadyExistsException;
import com.sprint.mission.discodeit.io.InputHandler;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
//
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
//
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;

  //
  private final BinaryContentService binaryContentService;
  //
  private final UserMapper userMapper;
  //
  private final InputHandler inputHandler;
  //
  private final PasswordEncoder passwordEncoder;
  //
  private final ApplicationEventPublisher eventPublisher;


  @CacheEvict(value = "users", allEntries = true)
  @Transactional
  @Override
  public UserDto createUser(UserCreateRequest userCreateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {

    log.info("사용자 생성 시도: username={}", userCreateRequest.username());
    log.debug("사용자 생성 시도: userCreateRequest={}, optionalProfileCreateRequest={}", userCreateRequest,
        optionalProfileCreateRequest);

    // 1. 유효성 검증
    if (userRepository.existsByUsername(userCreateRequest.username())) {
      log.warn("이미 존재하는 유저 이름: username={}", userCreateRequest.username());
      throw new UsernameAlreadyExistsException(Map.of("username", userCreateRequest.username()));
    }
    if (userRepository.existsByEmail(userCreateRequest.email())) {
      log.warn("이미 존재하는 이메일: email={}", userCreateRequest.email());
      throw new EmailAlreadyExistsException(
          Map.of("email", userCreateRequest.email()));
    }

    // 2. 프로필 이미지 객체 생성
    BinaryContent nullableProfile = optionalProfileCreateRequest.map(
        binaryContentService::createBinaryContent).orElse(null);

    // 3. 유저 객체 생성및 저장
    User user = User.builder()
        .username(userCreateRequest.username())
        .email(userCreateRequest.email())
        .password(passwordEncoder.encode(userCreateRequest.password()))
        .profile(nullableProfile)
        .role(Role.USER)
        .build();
    user = userRepository.save(user);

    if (nullableProfile != null) {
      ProfileUploadDto uploadDto = ProfileUploadDto.builder()
          .id(nullableProfile.getId())
          .bytes(optionalProfileCreateRequest.get().bytes())
          .userId(user.getId())
          .build();
      // 5. 커밋 후 프로필 이미지 업로드 및 알림 관련 이벤트 발행
      eventPublisher.publishEvent(uploadDto);
    }

    // 4. userDto 반환
    log.info("사용자 생성 시도 성공: username={}, createdAt={}", user.getUsername(),
        user.getCreatedAt());
    return userMapper.toDto(user);
  }

  @Cacheable("users")
  @Override
  public List<UserDto> showAllUsers() {
    // TODO : 예외 처리
    return userRepository.findAll().stream()
        .map(userMapper::toDto)
        .toList();
  }

  @Override
  public UserDto getUserById(UUID id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException(Map.of("UserId", id)));
    return userMapper.toDto(user);
  }

  @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
  @Transactional
  @Override
  public UserDto updateUserInfo(UUID id, UserUpdateRequest userUpdateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {

    // boolean isUpdated = false; JPA 의 더티 채킹으로 save 하지 않아도 DB에 자동 업데이트

    User user = userRepository.findById(id)
        .orElseThrow(() -> {
          log.error("유저 수정 단계에서 유저를 찾지 못함: userId={}", id);
          return new UserNotFoundException(Map.of("UserId", id));
        });

    log.info("유저 수정 시도: originalUsername={}", user.getUsername());

    // 1. 유저 데이터 수정
    if (userUpdateRequest.newUsername() != null) {
      if (userRepository.existsByUsername(userUpdateRequest.newUsername())) {
        log.warn("이미 존재하는 유저 이름: username={}", userUpdateRequest.newUsername());
        throw new UsernameAlreadyExistsException(
            Map.of("username", userUpdateRequest.newUsername()));
      }
      user.updateUsername(userUpdateRequest.newUsername());
      log.info("유저 이름 수정: newUsername={}", userUpdateRequest.newUsername());
    }
    if (userUpdateRequest.newEmail() != null) {
      if (userRepository.existsByEmail(userUpdateRequest.newEmail())) {
        log.warn("이미 존재하는 이메일: email={}", userUpdateRequest.newEmail());
        throw new EmailAlreadyExistsException(
            Map.of("email", userUpdateRequest.newEmail()));
      }
      user.updateEmail(userUpdateRequest.newEmail());
      log.info("유저 이메일 수정: newEmail={}", userUpdateRequest.newEmail());
    }
    if (userUpdateRequest.newPassword() != null) {
      user.updatePassword(userUpdateRequest.newPassword());
      log.info("유저 패스워드 수정");
    }

    // 2. 프로필 이미지 수정

    if (optionalProfileCreateRequest.isPresent()) {
      BinaryContent nullableProfile = optionalProfileCreateRequest.map(
          binaryContentService::createBinaryContent).orElse(null);

      user.updateProfile(nullableProfile);

      ProfileUploadDto uploadDto = ProfileUploadDto.builder()
          .id(nullableProfile.getId())
          .bytes(optionalProfileCreateRequest.get().bytes())
          .userId(user.getId())
          .build();

      // 4. 커밋 후 프로필 이미지 업로드 및 알림 관련 이벤트 발행
      eventPublisher.publishEvent(uploadDto);
    }

    // 3. userDto 반환
    log.info("사용자 수정 시도 성공: username={}, updatedAt={}", user.getUsername(), user.getUpdatedAt());
    return userMapper.toDto(user);
  }

  @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
  @Transactional
  @Override
  public void removeUserById(UUID id) {
    log.info("유저 삭제 시도");
    String keyword = inputHandler.getYesNOInput();
    if (keyword.equalsIgnoreCase("y")) {
      // 유저 찾기
      User user = userRepository.findById(id)
          .orElseThrow(() -> {
            log.error("유저 삭제 단계에서 유저를 찾지 못함: userId={}", id);
            return new UserNotFoundException(Map.of("UserId", id));
          });
      if (user.getProfile() != null) {
        log.info("유저 프로필 이미지 삭제: profileName={}", user.getProfile().getFileName());
        // 유저의 프로필 이미지 삭제
        binaryContentService.deleteBinaryContentById(user.getProfile().getId());
      }

      // 유저 삭제
      userRepository.deleteById(id);
      log.info("유저 삭제 시도 성공");
    }
  }
}
