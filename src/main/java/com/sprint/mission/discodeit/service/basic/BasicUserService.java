package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontetnt.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final UserStatusRepository userStatusRepository;

  @Override
  public UserResponse createUser(CreateUserRequest request,
      Optional<CreateBinaryContentRequest> optionalRequest) {
    if (userRepository.existsUserByUsername(request.username()) || userRepository.existsUserByEmail(
        request.email())) {
      throw new IllegalArgumentException("이미 사용 중인 username 또는 email입니다.");
    }

    User user = new User(request.username(), request.password(), request.email());
    optionalRequest.map(this::saveBinaryContent).ifPresent(user::setProfileImage);

    UserStatus userStatus = new UserStatus(user, Instant.now());
    user.setUserStatus(userStatus);

    return UserResponse.fromEntity(userRepository.save(user));
  }

  @Override
  public List<UserResponse> findAllUsers() {
    return userRepository.findAll().stream().map(UserResponse::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  public Optional<UserResponse> findUserById(UUID userId) {
    return userRepository.findById(userId).map(UserResponse::fromEntity);
  }

  @Override
  public Optional<UserResponse> updateUser(UUID userId, UpdateUserRequest request,
      Optional<CreateBinaryContentRequest> optionalRequest) {
    return userRepository.findById(userId).map(user -> {
      if (request.username() != null) {
        user.setUsername(request.username());
      }
      optionalRequest.map(this::saveBinaryContent).ifPresent(user::setProfileImage);
      userRepository.save(user);

      return UserResponse.fromEntity(user);
    });
  }

  @Override
  public void deleteUser(UUID userId) {
    userRepository.findById(userId).ifPresent(user -> {
      if (user.getProfileImage() != null) {
        binaryContentRepository.deleteById(user.getProfileImage().getId());
      }
      if (user.getUserStatus() != null) {
        userStatusRepository.deleteById(user.getUserStatus().getId());
      }
      userRepository.deleteById(user.getId());
    });
  }

  @Override
  public User getUserById(UUID uuid) {
    return userRepository.findById(uuid)
        .orElseThrow(() -> new EntityNotFoundException("User with ID " + uuid + " not found"));
  }

  private BinaryContent saveBinaryContent(CreateBinaryContentRequest profileRequest) {
    String fileName = profileRequest.fileName();
    String contentType = profileRequest.contentType();
    byte[] bytes = profileRequest.bytes();
    BinaryContent binaryContent = new BinaryContent(fileName, contentType, bytes);
    return binaryContentRepository.save(binaryContent);
  }
}
