package com.sprint.mission.discodeit.service.facade;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.InvalidOperationException;
import com.sprint.mission.discodeit.exception.UserValidationException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.util.PasswordEncryptor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.constant.ErrorConstant.DEFAULT_ERROR_MESSAGE;

@Component
@RequiredArgsConstructor
public class BasicUserFacade implements UserFacade {
  private final UserMapper userMapper;
  private final BinaryContentMapper binaryContentMapper;
  private final UserService userService;
  private final BinaryContentService binaryContentService;
  private final UserStatusService userStatusService;

  @Override
  public UserResponseDto createUser(CreateUserRequest request) {
    User user = userMapper.from(request);
    user = userService.saveUser(user);

    UserStatus userStatus = userStatusService.create(new UserStatus(user.getUUID(), Instant.now()));

    BinaryContent profileImage = null;
    if (request.profileImage() != null && !request.profileImage().isEmpty()) {
      try {
        profileImage = binaryContentMapper.toProfilePicture(request.profileImage(), user.getUUID());
        user.setProfileImage(profileImage);
      } catch (IOException e) {
        throw new InvalidOperationException(DEFAULT_ERROR_MESSAGE);
      }
    }

    user.setStatus(userStatus);

    user = userService.update(user);

    return userMapper.from(user, userStatus, profileImage);
  }

  @Override
  public UserResponseDto findUserById(String id) {
    User user = userService.findUserById(id);
    UserStatus status = user.getStatus();
    BinaryContent content = user.getProfileImage();

    return userMapper.from(user, status, content);
  }

  @Override
  public UserResponseDto updateUser(String userId, UserUpdateDto updateDto, String plainPassword) {

    User user = userService.updateUser(userId, updateDto, plainPassword);

    BinaryContent profileImage = null;
    if (updateDto.profileImage() != null && !updateDto.profileImage().isEmpty()) {
      if (user.getProfileImage() != null) {
        binaryContentService.delete(user.getProfileImage().getUUID());
      }

      try {
        profileImage = binaryContentMapper.toProfilePicture(updateDto.profileImage(), user.getUUID());
        user.setProfileImage(profileImage);
      } catch (IOException e) {
        throw new InvalidOperationException(DEFAULT_ERROR_MESSAGE);
      }
    }

    User updatedUser = userService.update(user);

    return UserResponseDto.from(updatedUser, updatedUser.getStatus(), updatedUser.getProfileImage());
  }

  @Override
  public List<UserResponseDto> findAllUsers() {
    List<User> users = userService.findAllUsers();
    Set<String> userIdSet = mapToUserUuids(users);
    Map<String, UserStatus> userStatusMap = userStatusService.mapUserToUserStatus(userIdSet);
    Map<String, BinaryContent> binaryContentMap = binaryContentService.mapUserToBinaryContent(userIdSet);

    return createMultipleUserResponses(users, userStatusMap, binaryContentMap);
  }

  @Override
  public void deleteUser(String id, String password) {
    User user = userService.findUserById(id);

    userService.deleteUser(id, password);
    userStatusService.deleteByUserId(id);
    if(user.getProfileImage()!=null){
      binaryContentService.delete(user.getProfileImage().getUUID());
    }
  }

  private Set<String> mapToUserUuids(List<User> users) {
    return users.stream()
        .map(User::getUUID)
        .collect(Collectors.toSet());
  }

  private List<UserResponseDto> createMultipleUserResponses(
      List<User> users,
      Map<String, UserStatus> userStatusMap,
      Map<String, BinaryContent> binaryContentMap
  ) {
    return users.stream()
        .map(user -> {
          UserStatus userStatus = getOrCreateUserStatus(userStatusMap, user.getUUID());
          BinaryContent profilePicture = binaryContentMap.getOrDefault(user.getUUID(), null);
          return UserResponseDto.from(user, userStatus, profilePicture);
        }).toList();
  }

  private UserStatus getOrCreateUserStatus(Map<String, UserStatus> userStatusMap, String userId) {
    return userStatusMap.containsKey(userId)
        ? userStatusMap.get(userId)
        : userStatusService.create(new UserStatus(userId, Instant.now()));
  }
}
