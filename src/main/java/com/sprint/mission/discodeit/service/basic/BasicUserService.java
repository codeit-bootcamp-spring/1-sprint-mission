package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.user.UserStatusCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.io.InputHandler;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
//
import com.sprint.mission.discodeit.service.UserStatusService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
//
import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  //
  private final BinaryContentService binaryContentService;
  private final UserStatusService userStatusService;
  //
  private final UserMapper userMapper;
  private final BinaryContentMapper binaryContentMapper;
  //
  private final InputHandler inputHandler;

  @Transactional
  @Override
  public UserDto createUser(UserCreateRequest userCreateRequest,
      BinaryContentCreateRequest binaryContentCreateRequest) {

    log.info("사용자 생성 시도: username={}", userCreateRequest.username());

    // username과 email이 다른 유저와 같이 겹치는지 검증
    if (userRepository.existsByUsername(userCreateRequest.username())) {
      log.warn("이미 존재하는 유저: username={}", userCreateRequest.username());
      throw new IllegalArgumentException("동일한 username이 존재합니다."); // 전역 에러에서 400 처리
    }
    if (userRepository.existsByEmail(userCreateRequest.email())) {
      log.warn("이미 존재하는 이메일: email={}", userCreateRequest.email());
      throw new IllegalArgumentException("동일한 email이 존재합니다."); // 전역 에러에서 400 처리
    }

    // 프로필 이미지 생성 : BinaryContent 도메인 객체 생성
    if (binaryContentCreateRequest != null) {
      log.info("프로필 이미지 생성 시도: fileName={}, contentType={} ",
          binaryContentCreateRequest.fileName(),
          binaryContentCreateRequest.contentType());
      binaryContentService.createBinaryContent(binaryContentCreateRequest);
    }

    // 유저 생성 : User 도메인 객체 생성
    User user = User.builder()
        .username(userCreateRequest.username())
        .email(userCreateRequest.email())
        .password(userCreateRequest.password())
        .build();

    log.info("유저 상태 생성 시도");
    // 유저 상태 생성 : UserStatus 도메인 객체 생성
    UserStatusCreateRequest userStatusCreateRequest =
        new UserStatusCreateRequest(
            user,
            Instant.now());
    userStatusService.createUserStatus(userStatusCreateRequest);

    // todo: User 생성시 UserStatus, BinaryContent 지정하기

    userRepository.save(user);

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
        .orElseThrow(() -> new NoSuchElementException("유저(" + id + ")가 없습니다."));
    return userMapper.toDto(user);
  }


  @Transactional
  @Override
  public UserDto updateUserInfo(UUID id, UserUpdateRequest userUpdateRequest,
      BinaryContentCreateRequest binaryContentCreateRequest) {

    // boolean isUpdated = false; JPA 의 더티 채킹으로 save 하지 않아도 DB에 자동 업데이트

    // 유저 존재 유무 확인
    User user = userRepository.findById(id)
        .orElseThrow(() -> {
          log.error("유저 수정 단계에서 유저를 찾지 못함: userId={}", id);
          return new NoSuchElementException("유저(" + id + ")를 찾지 못했습니다.");
        });

    log.info("유저 수정 시도: originalUsername={}", user.getUsername());

    // 유저 데이터 수정
    if (userUpdateRequest.newUsername() != null) {
      if (userRepository.existsByUsername(userUpdateRequest.newUsername())) {
        log.warn("이미 존재하는 유저: username={}", userUpdateRequest.newUsername());
        throw new IllegalArgumentException("같은 username가 존재합니다.");
      }
      user.updateUsername(userUpdateRequest.newUsername());
      log.info("유저 이름 수정: newUsername={}", userUpdateRequest.newUsername());
    }
    if (userUpdateRequest.newEmail() != null) {
      if (userRepository.existsByEmail(userUpdateRequest.newEmail())) {
        log.warn("이미 존재하는 이메일: email={}", userUpdateRequest.newEmail());
        throw new IllegalArgumentException("같은 email이 존재합니다.");
      }
      user.updateEmail(userUpdateRequest.newEmail());
      log.info("유저 이메일 수정: newEmail={}", userUpdateRequest.newEmail());
    }
    if (userUpdateRequest.newPassword() != null) {
      user.updatePassword(userUpdateRequest.newPassword());
      log.info("유저 패스워드 수정");
    }

    // 프로필 이미지 수정
    if (binaryContentCreateRequest != null) {
      log.info("프로필 이미지 생성 시도: fileName={}, contentType={} ",
          binaryContentCreateRequest.fileName(),
          binaryContentCreateRequest.contentType());
      user.updateProfile(binaryContentMapper.toEntity(
          binaryContentService.createBinaryContent(binaryContentCreateRequest))
      );
    }
    log.info("사용자 수정 시도 성공: username={}, updatedAt={}", user.getUsername(), user.getUpdatedAt());
    return userMapper.toDto(user);
  }

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
            return new NoSuchElementException("유저(" + id + ")가 없습니다.");
          });
      log.info("유저 상태 삭제");
      // 유저 상태 삭제
      userStatusService.delteUserStatusByUserId(user.getId());
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
