package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user_status.UpdateUserStatusDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.InvalidOperationException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.validator.EntityValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.sprint.mission.discodeit.constant.ErrorConstant.DEFAULT_ERROR_MESSAGE;

@Service
@RequiredArgsConstructor
public class UserStatusServiceImpl implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final EntityValidator validator;

  @Override
  public UserStatus create(UserStatus status) {

    validator.findOrThrow(User.class, status.getUserId(), new UserNotFoundException());

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
    validator.findOrThrow(User.class, userId, new UserNotFoundException());
    return userStatusRepository.findByUserId(userId).orElseGet(() -> create(new UserStatus(userId, Instant.now())));
  }

  @Override
  public List<UserStatus> findAll() {
    return userStatusRepository.findAll();
  }


  @Override
  public UserStatus updateByUserId(String userId, UpdateUserStatusDto dto) {

    validator.findOrThrow(User.class, userId, new UserNotFoundException());

    UserStatus status = userStatusRepository.findByUserId(userId).orElseThrow(
        () -> new InvalidOperationException(DEFAULT_ERROR_MESSAGE)
    );

    status.setUserStatus(dto.status());

    userStatusRepository.save(status);

    return status;
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
