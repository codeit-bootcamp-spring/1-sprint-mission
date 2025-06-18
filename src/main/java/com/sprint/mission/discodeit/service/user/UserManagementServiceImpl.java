package com.sprint.mission.discodeit.service.user;

import com.sprint.mission.discodeit.async.binary_content.BinaryContentStorageWrapperService;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.UploadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.event.event_entity.BinaryContentUploadEvent;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.service.BinaryContentService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
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
  private final BinaryContentStorageWrapperService binaryContentStorageAsyncService;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  @Transactional
  public User createUser(User user, MultipartFile profile) {
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


  private void withProfile(User user, MultipartFile file) {

    try {
      log.debug("[SAVING USER PROFILE] : [USERNAME: {}]", user.getUsername());
      BinaryContent profile = binaryContentMapper.toProfileBinaryContent(file);
      profile.changeUploadStatus(UploadStatus.WAITING);
      user.updateProfileImage(profile);

      BinaryContent savedProfile = binaryContentService.save(profile, file.getBytes());

      eventPublisher.publishEvent(
          new BinaryContentUploadEvent(new ArrayList<>(List.of(savedProfile)),
              new ArrayList<>(List.of(file)), user));

    } catch (IOException e) {
      log.warn("[ERROR WHILE SAVING PROFILE] : {}", e.getMessage());
    }

  }
}
