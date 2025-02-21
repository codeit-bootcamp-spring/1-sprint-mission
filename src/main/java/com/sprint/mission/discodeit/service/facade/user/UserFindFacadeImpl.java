package com.sprint.mission.discodeit.service.facade.user;

import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserFindFacadeImpl implements UserFindFacade {

  private final UserService userService;
  private final UserMapper userMapper;

  private final UserStatusService userStatusService;
  private final BinaryContentService binaryContentService;
  private final BinaryContentMapper binaryContentMapper;

  @Override
  public UserResponseDto findUserById(String id) {
    User user = userService.findUserById(id);

    return userMapper.toDto(user);
  }

  @Override
  public List<UserResponseDto> findAllUsers() {

    List<User> users = userService.findAllUsers();


    // jpa 사용시 아래 삭제
    Set<String> userIdSt = mapToUserUuids(users);
    Map<String, UserStatus> userStatusMap = userStatusService.mapUserToUserStatus(userIdSt);
    Map<String, BinaryContent> binaryContentMap = binaryContentService.mapUserToBinaryContent(userIdSt);

    return createMultipleUserResponses(users, userStatusMap, binaryContentMap);
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
        .map(userMapper::toDto).toList();
  }
}
