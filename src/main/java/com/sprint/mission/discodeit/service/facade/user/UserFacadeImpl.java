package com.sprint.mission.discodeit.service.facade.user;

import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.service.basic.PermissionService;
import com.sprint.mission.discodeit.service.basic.UserOnlineStatusService;
import com.sprint.mission.discodeit.service.user.UserManagementService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserFacadeImpl implements UserFacade {

  private final PermissionService permissionService;
  private final UserManagementService userManagementService;
  private final UserMapper userMapper;
  private final PasswordEncoder encoder;
  private final UserOnlineStatusService onlineStatusService;


  @Override
  @Transactional
  public UserDto createUser(CreateUserRequest request, MultipartFile profile) {
    User user = userMapper.toEntity(request, encoder);
    User createdUser = userManagementService.createUser(user, profile);
    log.debug("[USER CREATED] : [USERNAME: {}]", createdUser.getUsername());

    return userMapper.toDto(createdUser, onlineStatusService);

  }

  @Override
  @Transactional(readOnly = true)
  public UserDto findUserById(String id) {
    User user = userManagementService.findSingleUser(id);

    return userMapper.toDto(user, onlineStatusService);
  }

  @Override
  @Transactional

  public UserDto updateUser(String userId, MultipartFile profile,
      UserUpdateDto updateDto, UserDetails userDetails) {
    log.debug("[UPDATE USER REQUEST] : [ID: {}]", userId);

    permissionService.checkIsAdminOrMe(UUID.fromString(userId), userDetails);

    User tmpUser = userMapper.toEntity(updateDto, encoder);
    User updatedUser = userManagementService.updateUser(userId, tmpUser, profile);
    log.debug("[UPDATED USER] : [ID : {}]", userId);

    return userMapper.toDto(updatedUser, onlineStatusService);
  }

  @Override
  @Transactional(readOnly = true)
  public List<UserDto> findAllUsers() {
    List<User> users = userManagementService.findAllUsers();
    return userMapper.toDtoList(users, onlineStatusService);

  }

  @Override
  @Transactional
  public void deleteUser(String id) {
    userManagementService.deleteUser(id);
  }
}
