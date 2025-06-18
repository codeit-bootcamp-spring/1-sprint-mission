package com.sprint.mission.discodeit.service.basic;


import com.sprint.mission.discodeit.dto.user.RoleUpdateRequest;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserRole;
import com.sprint.mission.discodeit.error.ErrorCode;
import com.sprint.mission.discodeit.event.event_entity.NotificationEvent;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.user.UserService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Service
@ConditionalOnProperty(name = "app.service.type", havingValue = "basic")
@RequiredArgsConstructor
public class BasicUserService implements UserService {

  private final UserRepository userRepository;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  @Transactional
  public User saveUser(User user) {
    return userRepository.save(user);
  }

  @Override
  @Transactional
  public User update(User user) {
    return userRepository.save(user);
  }

  @Override
  @Transactional(readOnly = true)
  public User findUserById(String id) {
    return userRepository.findById(UUID.fromString(id)).orElseThrow(
        () -> {
          log.debug("[USER NOT FOUND] [ID: {}]", id);
          return new UserNotFoundException(ErrorCode.USER_NOT_FOUND, Map.of("userId", id));
        }
    );
  }

  @Override
  @Transactional(readOnly = true)
  public List<User> findAllUsers() {
    return userRepository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public List<User> findAllUsersIn(List<String> userIds) {
    log.debug("[VALIDATING USERS] : [ID: {}]", userIds);

    List<UUID> userUuids = parseStringToUuid(userIds);

    return userRepository.findAllByIdIn(userUuids);
  }

  @Override
  @Transactional(readOnly = true)
  public List<User> findByAllIn(List<UUID> userIds) {
    return userRepository.findAllByIdIn(userIds);
  }

  @Override
  @Transactional
  public void deleteUser(String id) {

    UUID userId = null;

    try {
      userId = UUID.fromString(id);
    } catch (IllegalArgumentException e) {
      throw new DiscodeitException(ErrorCode.INVALID_UUID_FORMAT);
    }

    try {
      userRepository.deleteById(userId);
    } catch (EmptyResultDataAccessException ignored) {
      log.warn("[USER DELETE IGNORED] No user found for ID: {}", id);
    }
  }

  private List<UUID> parseStringToUuid(List<String> userIds) {
    List<UUID> userUuids = new ArrayList<>();

    for (String userId : userIds) {
      try {
        userUuids.add(UUID.fromString(userId));
      } catch (IllegalArgumentException e) {
        log.warn("[INVALID UUID FORMAT] : [ID: {}]", userId);
        throw new DiscodeitException(ErrorCode.INVALID_UUID_FORMAT,
            Map.of("invalidUserId", userId));
      }
    }

    return userUuids;

  }


  @Override
  @Transactional
  public User updateUserRole(RoleUpdateRequest request) {

    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> new UserNotFoundException(ErrorCode.USER_NOT_FOUND));

    UserRole prevRole = user.getRole();

    user.updateUserRole(request.newRole());

    NotificationEvent event = new NotificationEvent(
        List.of(user),
        NotificationType.ROLE_CHANGED,
        user.getId(),
        "권한이 변경되었습니다.",
        prevRole.toString() + " -> " + user.getRole().toString()
    );

    eventPublisher.publishEvent(event);
    return user;
  }

}
