package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.UserDto;
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
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.RoleRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
//
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;
  //
  private final PasswordEncoder passwordEncoder;
  private final RoleRepository roleRepository;

  @Transactional
  @Override
  public UserDto createUser(UserCreateRequest userCreateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {

    log.info("사용자 생성 시도: username={}", userCreateRequest.username());
    log.debug("사용자 생성 시도: userCreateRequest={}, optionalProfileCreateRequest={}", userCreateRequest,
        optionalProfileCreateRequest);

    if (userRepository.existsByUsername(userCreateRequest.username())) {
      log.warn("이미 존재하는 유저 이름: username={}", userCreateRequest.username());
      throw new UsernameAlreadyExistsException(Map.of("username", userCreateRequest.username()));
    }
    if (userRepository.existsByEmail(userCreateRequest.email())) {
      log.warn("이미 존재하는 이메일: email={}", userCreateRequest.email());
      throw new EmailAlreadyExistsException(
          Map.of("email", userCreateRequest.email()));
    }

    BinaryContent nullableProfile =
        optionalProfileCreateRequest.map(
                profileRequest -> {
                  log.info("프로필 이미지 생성 시도: fileName={}, contentType={} ",
                      profileRequest.fileName(),
                      profileRequest.contentType());
                  BinaryContent binaryContent = BinaryContent.builder()
                      .fileName(profileRequest.fileName())
                      .size(profileRequest.size())
                      .contentType(profileRequest.contentType())
                      .build();
                  BinaryContent content = binaryContentRepository.save(binaryContent);
                  binaryContentStorage.put(content.getId(), profileRequest.bytes());
                  return content;
                })
            .orElse(null);

    // 권한 생성
    Role role = roleRepository.findByName("ROLE_USER")
        .orElseThrow(() -> new RuntimeException("ROLE_USER 가 존재하지 않습니다."));
    // 유저 생성 : User 도메인 객체 생성
    User user = User.builder()
        .username(userCreateRequest.username())
        .email(userCreateRequest.email())
        .password(passwordEncoder.encode(userCreateRequest.password()))
        .profile(nullableProfile)
        .build();

    user = userRepository.save(user);

    /* 중복이 없는 유저 이름과 만들어진 시각을 log.info에 담는다.*/
    log.info("사용자 생성 시도 성공: username={}, createdAt={}", user.getUsername(), user.getCreatedAt());
    return userMapper.toDto(user);
  }

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

    // 유저 존재 유무 확인
    User user = userRepository.findById(id)
        .orElseThrow(() -> {
          log.error("유저 수정 단계에서 유저를 찾지 못함: userId={}", id);
          return new UserNotFoundException(Map.of("UserId", id));
        });

    log.info("유저 수정 시도: originalUsername={}", user.getUsername());

    // 유저 데이터 수정
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

    // 프로필 이미지 수정

    BinaryContent nullableProfile =
        optionalProfileCreateRequest.map(
                profileRequest -> {
                  log.info("프로필 이미지 생성 시도: fileName={}, contentType={} ",
                      profileRequest.fileName(),
                      profileRequest.contentType());
                  BinaryContent binaryContent = BinaryContent.builder()
                      .fileName(profileRequest.fileName())
                      .size(profileRequest.size())
                      .contentType(profileRequest.contentType())
                      .build();
                  BinaryContent content = binaryContentRepository.save(binaryContent);
                  binaryContentStorage.put(content.getId(), profileRequest.bytes());
                  return content;
                })
            .orElse(null);
    user.updateProfile(nullableProfile);

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
