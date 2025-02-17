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

  private final UserCreationFacade userCreationFacade;
  private final UserUpdateFacade userUpdateFacade;
  private final UserFindFacade userFindFacade;
  private final UserDeleteFacade userDeleteFacade;

  @Override
  public UserResponseDto createUser(CreateUserRequest request) {
    return userCreationFacade.createUser(request);
  }

  @Override
  public UserResponseDto findUserById(String id) {
    return userFindFacade.findUserById(id);
  }

  @Override
  public UserResponseDto updateUser(String userId, UserUpdateDto updateDto) {
    return userUpdateFacade.updateUser(userId, updateDto);
  }

  @Override
  public List<UserResponseDto> findAllUsers() {
    return userFindFacade.findAllUsers();
  }

  @Override
  public void deleteUser(String id, String password) {
    userDeleteFacade.delete(id, password);
  }
}
