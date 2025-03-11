package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserRequest;
import com.sprint.mission.discodeit.dto.UserResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.RestApiException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.validation.UserValidator;
import jakarta.transaction.Transactional;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserValidator userValidator;
  private final UserStatusService userStatusService;
  private final BinaryContentRepository binaryContentRepository;

  @Override
  @Transactional
  public UserResponse createUser(UserRequest request, MultipartFile userProfileImage) {
    if (userValidator.isValidName(request.name()) && userValidator.isValidEmail(request.email())
        && userValidator.isValidPassword(request.password())) {

      BinaryContent newProfile = Optional.ofNullable(userProfileImage)
          .map(profile ->
              binaryContentRepository.save(BinaryContent.createBinaryContent(
                  profile.getName(),
                  profile.getSize(),
                  profile.getContentType(),
                  convertToBytes(profile))))
          .orElse(null);

      User newUser = User.createUser(request.name(), request.email(), request.password(),
          newProfile);
      userRepository.save(newUser);
      userStatusService.create(newUser.getId());

      log.info("Create User: {}", newUser);
      return UserResponse.entityToDto(newUser);
    }
    return null;
  }

  @Override
  public List<UserResponse> findAll() {
    return userRepository.findAll().stream()
        .map(UserResponse::entityToDto)
        .collect(Collectors.toList());
  }

  @Override
  public UserResponse findById(UUID id) {
    return UserResponse.entityToDto(findByIdOrThrow(id));
  }

  @Override
  @Transactional
  public UserResponse update(UUID id, UserRequest request, MultipartFile userProfileImage) {
    User user = findByIdOrThrow(id);

    if (userValidator.isValidName(request.name()) && userValidator.isValidEmail(request.email())
        && userValidator.isValidPassword(request.password())) {

      Optional.ofNullable(request.name()).ifPresent(user::updateName);
      Optional.ofNullable(request.email()).ifPresent(user::updateEmail);
      Optional.ofNullable(request.password()).ifPresent(user::updatePassword);
      Optional.ofNullable(userProfileImage)
          .ifPresent(profile -> {
            if (!profile.isEmpty()) { // 파라미터는 있는데, 파일이 안 들어올 때
              user.updateProfile(binaryContentRepository.save(
                  BinaryContent.createBinaryContent(
                      profile.getName(),
                      profile.getSize(),
                      profile.getContentType(),
                      convertToBytes(profile))));
            }
          });
    }
    log.info("Update User :{}", user);
    return UserResponse.entityToDto(user);
  }

  @Override
  public void deleteById(UUID id) {
    User user = findByIdOrThrow(id);
    binaryContentRepository.deleteById(user.getProfile().getId());
    userRepository.deleteById(id);
  }

  private User findByIdOrThrow(UUID id) {
    return userRepository.findById(id)
        .orElseThrow(() -> new RestApiException(ErrorCode.USER_NOT_FOUND, "id : " + id));
  }

  private byte[] convertToBytes(MultipartFile imageFile) {
    try {
      return imageFile.getBytes();
    } catch (IOException e) {
      throw new RestApiException(ErrorCode.INTERNAL_SERVER_ERROR, "변환 실패");
    }
  }

}
