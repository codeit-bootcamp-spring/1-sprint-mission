package com.sprint.mission.discodeit.service.facade.user;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.InvalidOperationException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.constant.ErrorConstant.DEFAULT_ERROR_MESSAGE;

@Slf4j
@Component
@RequiredArgsConstructor
public class BasicUserMasterFacade implements UserMasterFacade {
  private final UserMapper userMapper;
  private final BinaryContentMapper binaryContentMapper;
  private final UserService userService;
  private final BinaryContentService binaryContentService;
  private final UserStatusService userStatusService;

  private final UserCreationFacade userCreationFacade;
  @Override
  public UserResponseDto createUser(CreateUserRequest request) {
//
//    User user = userService.saveUser(userMapper.from(request));
//
//    UserStatus userStatus = userStatusService.create(new UserStatus(user.getUUID(), Instant.now()));
//
//
//    BinaryContent profileImage = null;
//    if (request.profileImage() != null && !request.profileImage().isEmpty()) {
//
//      profileImage = binaryContentMapper.toProfilePicture(request.profileImage(), user.getUUID());
//      user.setProfileImage(profileImage);
//      binaryContentService.create(profileImage);
//
//    }
//
//    user.setStatus(userStatus);
//
//    user = userService.update(user);
//
//    return userMapper.from(user, userStatus, profileImage);
    return userCreationFacade.createUser(request);
  }

  @Override
  public UserResponseDto findUserById(String id) {
    User user = userService.findUserById(id);
    UserStatus status = user.getStatus();
    BinaryContent content = user.getProfileImage();

    return userMapper.from(user, status, content);
  }

  // TODO : 사진 업데이트 안됨
  @Override
  public UserResponseDto updateUser(String userId, UserUpdateDto updateDto) {
    log.info("[User Update] 요청 수신 : userId={}, updateFields={}", userId, updateDto);
    User user = userService.updateUser(userId, updateDto, updateDto.inputPassword());
    log.info("[User Update] 사용자 정보 업데이트 완료: userId={}", userId);

    BinaryContent profileImage = null;
    if (updateDto.profileImage() != null && !updateDto.profileImage().isEmpty()) {
      log.info("[User Update] 프로필 이미지 변경 요청 확인: userId={}", userId);
      if (user.getProfileImage() != null) {
        log.info("[User Update] 기존 프로필 이미지 삭제: imageId={}", user.getProfileImage().getFileName());
        binaryContentService.delete(user.getProfileImage().getUUID());
      }


      profileImage = binaryContentMapper.toProfilePicture(updateDto.profileImage(), user.getUUID());
      user.setProfileImage(profileImage);
      binaryContentService.create(profileImage);
      log.info("[User Update] 새로운 프로필 이미지 저장 완료: userId={}, imageId={}, imageName={}", userId, profileImage.getUUID(), profileImage.getFileName());


    }

    User updatedUser = userService.update(user);
    log.info("[User Update] 사용자 최종 업데이트 완료: userId={}", userId);
    return userMapper.from(updatedUser, updatedUser.getStatus(), updatedUser.getProfileImage());
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
          return userMapper.from(user, userStatus, profilePicture);
        }).toList();
  }

  private UserStatus getOrCreateUserStatus(Map<String, UserStatus> userStatusMap, String userId) {
    return userStatusMap.containsKey(userId)
        ? userStatusMap.get(userId)
        : userStatusService.create(new UserStatus(userId, Instant.now()));
  }
}
