package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  //
  private final BinaryContentRepository binaryContentRepository;
  private final UserStatusRepository userStatusRepository;

  @Override
  @Transactional
  public User create(UserCreateRequest userCreateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    String username = userCreateRequest.username();
    String email = userCreateRequest.email();

    // 동시성 문제 방지를 위해 트랜잭션 레벨에서 중복 체크
    validateUniqueUserConstraints(username, email);

    // 프로필 이미지 처리 - 영속성 전이 고려
    BinaryContent profileContent = optionalProfileCreateRequest
        .map(this::createAndSaveBinaryContent)
        .orElse(null);

    // BinaryContent 객체를 직접 전달
    User user = new User(username, email, userCreateRequest.password(), profileContent);
    User createdUser = userRepository.save(user);

    // 사용자 상태 생성 - 즉시 저장
    createUserStatus(createdUser);

    return createdUser;
  }


  // 중복 체크 메서드 분리
  private void validateUniqueUserConstraints(String username, String email) {
    if (userRepository.existsByEmail(email)) {
      throw new IllegalArgumentException("User with email " + email + " already exists");
    }
    if (userRepository.existsByUsername(username)) {
      throw new IllegalArgumentException("User with username " + username + " already exists");
    }
  }

  // 바이너리 콘텐츠 생성 메서드 분리
  private BinaryContent createAndSaveBinaryContent(BinaryContentCreateRequest profileRequest) {
    BinaryContent profileContent = new BinaryContent(
        profileRequest.fileName(),
        (long) profileRequest.bytes().length,
        profileRequest.contentType(),
        profileRequest.bytes()
    );
    return binaryContentRepository.save(profileContent);
  }

  // 사용자 상태 생성 메서드 분리
  private void createUserStatus(User user) {
    UserStatus userStatus = new UserStatus(user, Instant.now());
    userStatusRepository.save(userStatus);
  }

  @Override
  public UserDto find(UUID userId) {
    return userRepository.findById(userId)
        .map(this::toDto)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));
  }

  @Override
  public List<UserDto> findAll() {
    return userRepository.findAll()
        .stream()
        .map(this::toDto)
        .toList();
  }

  @Override
  @Transactional
  public User update(UUID userId, UserUpdateRequest userUpdateRequest,
      Optional<BinaryContentCreateRequest> optionalProfileCreateRequest) {
    // 엔티티 조회 - 영속성 컨텍스트에서 관리되는 엔티티
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

    // 중복 체크
    validateUniqueUserConstraints(
        userUpdateRequest.newUsername(),
        userUpdateRequest.newEmail()
    );

    // 프로필 이미지 처리 - 기존 이미지 삭제 및 새 이미지 저장
    BinaryContent newProfile = optionalProfileCreateRequest
        .map(profileRequest -> {
          // 기존 프로필 이미지 삭제 (존재하는 경우)
          Optional.ofNullable(user.getProfile())
              .ifPresent(profile -> binaryContentRepository.deleteById(profile.getId()));

          // 새 프로필 이미지 생성 및 저장
          return createAndSaveBinaryContent(profileRequest);
        })
        .orElse(null);

    // 사용자 정보 업데이트 - 변경 감지(dirty checking) 활용
    user.update(
        userUpdateRequest.newUsername(),
        userUpdateRequest.newEmail(),
        userUpdateRequest.newPassword(),
        newProfile
    );

    // 트랜잭션 종료 시 변경 내용 자동 저장
    return user;
  }

  @Override
  @Transactional
  public void delete(UUID userId) {
    // 엔티티 조회 - 영속성 컨텍스트에서 관리되는 엔티티
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("User with id " + userId + " not found"));

    // 연관된 엔티티 삭제
    Optional.ofNullable(user.getProfile())
        .ifPresent(profile -> binaryContentRepository.deleteById(profile.getId()));

    userStatusRepository.deleteByUserId(userId);

    // 엔티티 삭제 - 영속성 컨텍스트에서 자동 처리
    userRepository.delete(user);
  }

  private UserDto toDto(User user) {
    // 지연 로딩을 고려한 상태 조회
    Boolean online = userStatusRepository.findByUserId(user.getId())
        .map(UserStatus::isOnline)
        .orElse(null);

    return new UserDto(
        user.getId(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getUsername(),
        user.getEmail(),
        user.getProfile() != null ? user.getProfile().getId() : null,
        online
    );
  }
}