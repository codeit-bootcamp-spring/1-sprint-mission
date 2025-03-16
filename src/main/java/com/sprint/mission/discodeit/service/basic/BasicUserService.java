package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binary_content.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDTO;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.transaction.Transactional;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  public static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final UserStatusRepository userStatusRepository;
  private final BinaryContentService binaryContentService;

  @Transactional
  @Override
  public User create(UserCreateRequest userCreateRequest, MultipartFile profile) {
    if (!isValidEmail(userCreateRequest.email())) {
      throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
    }

    if (userRepository.existsByUsername((userCreateRequest.userName()))) {
      throw new IllegalArgumentException("이미 존재하는 사용자 이름입니다.");
    }

    if (userRepository.existsByEmail((userCreateRequest.email()))) {
      throw new IllegalArgumentException("이미 존재하는 사용자 이메일입니다.");
    }

    BinaryContent nullableProfile = Optional.ofNullable(profile)
        .filter(p -> !p.isEmpty())
        .flatMap(this::resolveProfileRequest)
        .map(binaryContentService::create)
        .orElse(null);

    User user = new User(
        userCreateRequest.userName(),
        userCreateRequest.email(),
        userCreateRequest.password(),
        nullableProfile,
        null);
    userRepository.save(user);

    UserStatus userStatus = new UserStatus(user, Instant.EPOCH);
    userStatusRepository.save(userStatus);

    return user;
  }

  @Override
  public UserDTO find(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("유저가 존재하지 않습니다."));
    UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
        .orElseThrow(() -> new NoSuchElementException("유저상태가 존재하지 않습니다."));

    return UserDTO.fromEntity(user, userStatus);
  }

  @Override
  public List<UserDTO> findAll() {
    return userRepository.findAll()
        .stream()
        .map(user -> {
          UserStatus userStatus = userStatusRepository.findByUserId(user.getId())
              .orElseThrow(() -> new NoSuchElementException("유저상태가 존재하지 않습니다."));
          return UserDTO.fromEntity(user, userStatus);
        })
        .toList();
  }

  @Transactional
  @Override
  public User update(UUID userId, UserUpdateRequest userUpdateRequest, MultipartFile profile) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("유저가 존재하지 않습니다."));

    if (!user.getPassword().equals(userUpdateRequest.password())) {
      throw new IllegalArgumentException("비밀번호가 틀렸습니다.");
    }

    if (!isValidEmail(userUpdateRequest.newEmail())) {
      throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
    }

    if (userRepository.existsByUsername(userUpdateRequest.newUserName())) {
      throw new IllegalArgumentException("이미 존재하는 사용자 이름입니다.");
    }

    if (userRepository.existsByEmail(userUpdateRequest.newEmail())) {
      throw new IllegalArgumentException("이미 존재하는 사용자 이메일입니다.");
    }

    Optional<BinaryContent> newProfile = Optional.ofNullable(profile)
        .filter(p -> !p.isEmpty())
        .flatMap(this::resolveProfileRequest)
        .map(binaryContentService::create);

    newProfile.ifPresent(binaryContent -> {
      if (user.getProfile() != null) {
        binaryContentRepository.deleteById(user.getProfile().getId());
      }
      binaryContentRepository.save(binaryContent);
      user.updateProfile(binaryContent);
    });

    user.update(userUpdateRequest.newUserName(), userUpdateRequest.newEmail());
    return userRepository.save(user);
  }


  @Override
  public void delete(UUID userId) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new NoSuchElementException("유저가 존재하지 않습니다."));

    if (user.getProfile() != null && binaryContentRepository.existsById(
        user.getProfile().getId())) {
      binaryContentRepository.deleteById(user.getProfile().getId());
    }

    userStatusRepository.deleteById(userId);
    userRepository.deleteById(userId);
  }

  private Optional<BinaryContentCreateRequest> resolveProfileRequest(MultipartFile profileFile) {
    if (profileFile == null || profileFile.isEmpty()) {
      return Optional.empty();
    }
    try {
      return Optional.of(new BinaryContentCreateRequest(
          profileFile.getOriginalFilename(),
          profileFile.getContentType(),
          profileFile.getBytes()
      ));
    } catch (IOException e) {
      throw new RuntimeException("파일 업로드 중 오류 발생", e);
    }
  }

  private boolean isValidEmail(String email) {
    return email.matches(EMAIL_REGEX);
  }
}
