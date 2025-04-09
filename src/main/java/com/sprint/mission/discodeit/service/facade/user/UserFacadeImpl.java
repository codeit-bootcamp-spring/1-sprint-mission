package com.sprint.mission.discodeit.service.facade.user;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.service.user.UserManagementService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFacadeImpl implements UserFacade {

  private final UserManagementService userManagementService;
  private final UserMapper userMapper;

  @Override
  @Transactional
  public UserResponseDto createUser(CreateUserRequest request, MultipartFile profile) {
    User user = userMapper.toEntity(request);
    User createdUser = userManagementService.createUser(user, profile);
    log.debug("[USER CREATED] : [USERNAME: {}]", createdUser.getUsername());

    return userMapper.toDto(createdUser);
  }

  @Override
  @Transactional(readOnly = true)
  public UserResponseDto findUserById(String id) {
    User user = userManagementService.findSingleUser(id);
    return userMapper.toDto(user);
  }

  @Override
  @Transactional
  public UserResponseDto updateUser(String userId, MultipartFile profile, UserUpdateDto updateDto) {
    log.debug("[UPDATE USER REQUEST] : [ID: {}]", userId);
    User tmpUser = userMapper.toEntity(updateDto);
    User updatedUser = userManagementService.updateUser(userId, tmpUser, profile);
    log.debug("[UPDATED USER] : [ID : {}]", userId);

    return userMapper.toDto(updatedUser);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserResponseDto> findAllUsers() {
    List<User> users = userManagementService.findAllUsers();
    return userMapper.toDtoList(users);
  }

  @Override
  @Transactional
  public void deleteUser(String id) {
    userManagementService.deleteUser(id);
  }
}
