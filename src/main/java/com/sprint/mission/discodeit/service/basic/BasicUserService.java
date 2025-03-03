package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.UserCreateRequest;
import com.sprint.mission.discodeit.dto.UserDTO;
import com.sprint.mission.discodeit.dto.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
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

  @Override
  public User create(UserCreateRequest userCreateRequest, MultipartFile profile) {
    if (!isValidEmail(userCreateRequest.email())) {
      throw new IllegalArgumentException("이메일 형식이 올바르지 않습니다.");
    }

    if (userRepository.existsName(userCreateRequest.userName())) {
      throw new IllegalArgumentException("이미 존재하는 사용자 이름입니다.");
    }

    if (userRepository.existsEmail(userCreateRequest.email())) {
      throw new IllegalArgumentException("이미 존재하는 사용자 이메일입니다.");
    }

    Optional<BinaryContentCreateRequest> profileRequest = resolveProfileRequest(profile);
    UUID nullableProfileId = profileRequest
        .map(request -> binaryContentService.create(request).getId())
        .orElse(null);

    User user = new User(
        userCreateRequest.userName(),
        userCreateRequest.email(),
        userCreateRequest.password(),
        nullableProfileId);
    userRepository.save(user);

    UserStatus userStatus = new UserStatus(user.getId(), Instant.EPOCH);
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

    if (userRepository.existsName(userUpdateRequest.newUsername())) {
      throw new IllegalArgumentException("이미 존재하는 사용자 이름입니다.");
    }

    if (userRepository.existsEmail(userUpdateRequest.newEmail())) {
      throw new IllegalArgumentException("이미 존재하는 사용자 이메일입니다.");
    }

    Optional<BinaryContentCreateRequest> profileRequest = resolveProfileRequest(profile);
    if (profileRequest.isPresent()) {
      BinaryContentCreateRequest profileData = profileRequest.get();
      BinaryContent binaryContent = binaryContentService.create(profileData);
      binaryContentRepository.save(binaryContent);
    }

    user.update(userUpdateRequest.newUsername(), userUpdateRequest.newEmail());
    return userRepository.save(user);
  }

  @Override
  public void delete(UUID userId) {
    if (!userRepository.existsId(userId)) {
      throw new NoSuchElementException("유저가 존재하지 않습니다.");
    }

    if (binaryContentRepository.existsId(userId)) {
      binaryContentRepository.deleteById(userId);
    }

    userStatusRepository.deleteByUserId(userId);
    userRepository.delete(userId);
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
