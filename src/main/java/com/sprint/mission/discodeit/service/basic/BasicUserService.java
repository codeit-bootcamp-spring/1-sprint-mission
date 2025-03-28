package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserRequest;
import com.sprint.mission.discodeit.dto.UserResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.global.exception.ErrorCode;
import com.sprint.mission.discodeit.global.exception.RestApiException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.validation.UserValidator;
import jakarta.transaction.Transactional;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserValidator userValidator;
  private final UserMapper userMapper;
  private final UserStatusRepository userStatusRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  @Transactional
  public UserResponse createUser(UserRequest request, MultipartFile userProfileImage) {
    if (userValidator.isValidName(request.name()) && userValidator.isValidEmail(request.email())
        && userValidator.isValidPassword(request.password())) {

      BinaryContent newProfile = null;
      if (userProfileImage != null && !userProfileImage.isEmpty()) {
        newProfile = binaryContentRepository.save(BinaryContent.createBinaryContent(
            userProfileImage.getOriginalFilename(),
            userProfileImage.getSize(),
            userProfileImage.getContentType()));
        binaryContentStorage.put(newProfile.getId(), convertToBytes(userProfileImage));
      }

      User newUser = userRepository.save(User.createUser(
          request.name(), request.email(), request.password(), newProfile));
      UserStatus newUserStatus = userStatusRepository.save(UserStatus.createUserStatus(newUser));
      newUser.updateStatus(newUserStatus);

      log.info("Created user - id: {}", newUser.getId());
      return userMapper.entityToDto(newUser);
    }
    return null;
  }

  @Override
  public List<UserResponse> findAll() {
    return userRepository.findAll().stream()
        .map(userMapper::entityToDto)
        .collect(Collectors.toList());
  }

  @Override
  public UserResponse findById(UUID id) {
    return userMapper.entityToDto(findByIdOrThrow(id));
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
              BinaryContent binaryContent = binaryContentRepository.save(
                  BinaryContent.createBinaryContent(
                      profile.getOriginalFilename(),
                      profile.getSize(),
                      profile.getContentType()));
              binaryContentStorage.put(binaryContent.getId(), convertToBytes(profile));
              user.updateProfile(binaryContent);
            }
          });
    }
    log.info("Updated user - id: {}", user.getId());
    return userMapper.entityToDto(user);
  }

  @Override
  public void deleteById(UUID id) {
    findByIdOrThrow(id);
    userRepository.deleteById(id);
    log.info("Deleted user - id: {}", id);
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
