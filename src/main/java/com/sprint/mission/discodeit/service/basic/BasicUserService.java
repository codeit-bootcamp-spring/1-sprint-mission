package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.common.util.MultipartFileConverter;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.request.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.request.UpdateUserRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.validator.UserValidator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final UserStatusRepository userStatusRepository;
  private final BinaryContentRepository binaryContentRepository;

  private final UserMapper userMapper;

  private final UserValidator userValidator;

  private final MultipartFileConverter multipartFileConverter;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  @Transactional
  public UserDto createUser(CreateUserRequest createUserRequest, MultipartFile profileImageFile) {
    // name 중복 여부
    userValidator.validateDuplicateByName(createUserRequest.username());
    // email 중복 여부
    userValidator.validateDuplicateUserByEmail(createUserRequest.email());

    User user = userMapper.toEntity(createUserRequest);

    // 이미지는 선택적으로 등록
    updateProfileImage(user, profileImageFile);
    User savedUser = userRepository.saveUser(user);

    // UserStatus 생성(추후 service layer로 교체)
    UserStatus savedUserStatus = userStatusRepository.saveUserStatus(UserStatus.of(savedUser));
    savedUser.updateUserStatus(savedUserStatus);

    return userMapper.toUserDto(savedUser);
  }

  @Override
  @Transactional(readOnly = true)
  public UserDto findUserByIdOrThrow(UUID userId) {
    User foundUser = userValidator.validateUserExistsByUserId(userId);

    return userMapper.toUserDto(foundUser);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserDto> findAllUsers() {
    return userRepository.findAllUsers().stream()
        .map(user -> findUserByIdOrThrow(user.getId()))
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public UserDto updateUser(UUID userId, UpdateUserRequest updateUserRequest,
      MultipartFile profileImageFile) {
    User foundUser = userValidator.validateUserExistsByUserId(userId);

    foundUser.updateUserInfo(updateUserRequest.newUsername(), updateUserRequest.newEmail(),
        updateUserRequest.newPassword());

    updateProfileImage(foundUser, profileImageFile);

    return userMapper.toUserDto(foundUser);
  }

  @Override
  @Transactional
  public void deleteUser(UUID userId) {
    // 유저 존재 여부 확인
    userValidator.validateUserExistsByUserId(userId);

    // cascade 옵션으로 userStatus, binaryContent 자동 삭제

    // 유저 삭제
    userRepository.removeUser(userId);
  }

  private void updateProfileImage(User user, MultipartFile profileImageFile) {
    Optional.ofNullable(profileImageFile)
        .ifPresent(file -> {
          BinaryContent binaryContent = binaryContentRepository.saveBinaryContent(
              BinaryContent.of(file.getOriginalFilename(), file.getSize(), file.getContentType()));

          binaryContentStorage.put(binaryContent.getId(), multipartFileConverter.toByteArray(file));
          user.updateProfileImage(binaryContent);
        });
  }
}
