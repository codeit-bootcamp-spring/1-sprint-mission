package com.sprint.mission.discodeit.service.user;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.exception.file.FileException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Slf4j
@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {

  private final UserService userService;
  private final BinaryContentService binaryContentService;
  private final BinaryContentMapper binaryContentMapper;

  @Override
  @Transactional
  public User createUser(User user, MultipartFile profile) {
    setStatusToUser(user);
    if (profile != null && !profile.isEmpty()) {
      withProfile(user, profile);
    }
    return userService.saveUser(user);
  }

  @Override
  @Transactional
  public User updateUser(String userId, User tmpUser, MultipartFile profile) {
    User userToUpdate = userService.findUserById(userId);
    log.debug("[FOUND USER] [ID {}]", userId);

    userToUpdate.updateFields(tmpUser.getUsername(), tmpUser.getEmail(), tmpUser.getPassword());
    log.debug("[UPDATED USER FIELDS] : [ID: {}]", userId);

    if (profile != null && !profile.isEmpty()) {
      withProfile(userToUpdate, profile);
    }
    return userService.saveUser(userToUpdate);
  }

  @Override
  public User findSingleUser(String userId) {
    return userService.findUserById(userId);
  }

  @Override
  public List<User> findAllUsers() {
    return userService.findAllUsers();
  }

  @Override
  @Transactional
  public void deleteUser(String userId) {

    User user = userService.findUserById(userId);
    log.debug("[FOUND USER] : [ID: {}]", userId);
    userService.deleteUser(userId);

    log.debug("[DELETED USER] : [ID: {}]", userId);
    if (user.getProfile() != null) {
      binaryContentService.delete(String.valueOf(user.getProfile().getId()));
    }
  }

  private void setStatusToUser(User user) {
    UserStatus status = UserStatus.create(user);
    user.updateStatus(status);
  }


  private void withProfile(User user, MultipartFile file) {
    try {
      log.debug("[SAVING USER PROFILE] : [USERNAME: {}]", user.getUsername());
      BinaryContent profile = binaryContentMapper.toProfileBinaryContent(file);
      user.updateProfileImage(profile);
      binaryContentService.save(profile, file.getBytes());
      log.debug("[PROFILE SAVED] : [USERNAME: {}]", user.getUsername());
    } catch (IOException e) {
      // TODO : 저장된 파일 삭제
      log.warn("[ERROR DURING PROFILE SAVE] : [USERNAME: {}]", user.getUsername());
      throw new FileException(ErrorCode.FILE_ERROR,
          Map.of("username", user.getUsername(), "fileName", file.getOriginalFilename()));
    }
  }
}
