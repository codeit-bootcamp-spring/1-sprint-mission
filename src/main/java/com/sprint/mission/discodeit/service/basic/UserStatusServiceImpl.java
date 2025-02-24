package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user_status.UpdateUserStatusDto;
import com.sprint.mission.discodeit.dto.user_status.UserStatusResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.InvalidOperationException;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.util.UserStatusType;
import com.sprint.mission.discodeit.validator.EntityValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.constant.ErrorConstant.DEFAULT_ERROR_MESSAGE;
import static com.sprint.mission.discodeit.constant.UserConstant.NO_MATCHING_USER;

@Service
@RequiredArgsConstructor
public class UserStatusServiceImpl implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final UserRepository userRepository;
  private final EntityValidator validator;
  private final UserMapper userMapper;

  @Override
  public UserStatus create(UserStatus status) {
    validator.findOrThrow(User.class, status.getUserId(), new NotFoundException(NO_MATCHING_USER));

    if (userStatusRepository.findByUserId(status.getUserId()).isPresent()) {
      throw new InvalidOperationException(DEFAULT_ERROR_MESSAGE);
    }

    return userStatusRepository.save(status);
  }

  @Override
  public UserStatus find(String id) {
    return userStatusRepository.findById(id).orElseThrow(() -> new InvalidOperationException(DEFAULT_ERROR_MESSAGE));
  }

  @Override
  public UserStatus findByUserId(String userId) {
    validator.findOrThrow(User.class, userId, new NotFoundException(NO_MATCHING_USER));
    return userStatusRepository.findByUserId(userId).orElseGet(() -> create(new UserStatus(userId, Instant.now())));
  }

  @Override
  public List<UserStatus> findAll() {
    return userStatusRepository.findAll();
  }


  @Override
  public UserStatusResponseDto updateByUserId(String userId, UpdateUserStatusDto dto) {

    User user = validator.findOrThrow(User.class, userId,new NotFoundException(NO_MATCHING_USER));

    UserStatus status = userStatusRepository.findByUserId(userId).orElseThrow(
        () -> new InvalidOperationException(DEFAULT_ERROR_MESSAGE)
    );

    status.updateLastOnline(dto.newLastActiveAt());


    Instant now = Instant.now();
    long minutes = Duration.between(dto.newLastActiveAt(), now).toMinutes();

    if (minutes < 10) {
      status.setUserStatus(UserStatusType.ONLINE);
    } else if (minutes <= 60) {
      status.setUserStatus(UserStatusType.IDLE);
    } else {
      status.setUserStatus(UserStatusType.OFFLINE);
    }


    userStatusRepository.save(status);

    user.updateStatus(status);
    userRepository.update(user);

    return userMapper.withStatus(user);
  }

  @Override
  public Map<String, UserStatus> mapUserToUserStatus(Set<String> users) {

    if(users == null || users.isEmpty()) return Collections.emptyMap();

    List<UserStatus> statuses = userStatusRepository.findByAllIdIn(users);

    return statuses.stream()
        .collect(Collectors.toMap(UserStatus::getUserId, status -> status));

  }

  @Override
  public void delete(String id) {
    userStatusRepository.deleteById(id);
  }

  @Override
  public void deleteByUserId(String userId){
    userStatusRepository.deleteByUserId(userId);
  }
}
