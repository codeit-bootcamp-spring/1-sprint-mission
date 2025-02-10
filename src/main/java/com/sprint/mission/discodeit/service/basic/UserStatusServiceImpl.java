package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.user_status.CreateUserStatusDto;
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
import java.util.List;

import static com.sprint.mission.discodeit.constant.ErrorConstant.DEFAULT_ERROR_MESSAGE;

@Service
@RequiredArgsConstructor
public class UserStatusServiceImpl implements UserStatusService {

  private final UserStatusRepository userStatusRepository;
  private final EntityValidator validator;

  @Override
  public UserStatus create(CreateUserStatusDto dto) {

    validator.findOrThrow(User.class, dto.userId(), new UserNotFoundException());

    if (userStatusRepository.findByUserId(dto.userId()).isPresent()) {
      throw new InvalidOperationException(DEFAULT_ERROR_MESSAGE);
    }

    UserStatus userStatus = new UserStatus(dto.userId(), Instant.now());
    return userStatusRepository.save(userStatus);
  }

  @Override
  public UserStatus find(String id) {
    return userStatusRepository.findById(id).orElseThrow(() -> new InvalidOperationException(DEFAULT_ERROR_MESSAGE));
  }

  @Override
  public List<UserStatus> findAll() {
    return userStatusRepository.findAll();
  }

  @Override
  public UserStatus update(UpdateUserStatusDto dto) {

    UserStatus userStatus = find(dto.uuid());

    userStatus.updateLastOnline();

    userStatusRepository.save(userStatus);

    return userStatus;
  }

  @Override
  public UserStatus updateByUserId(String userId, UpdateUserStatusDto dto) {

    validator.findOrThrow(User.class, userId, new UserNotFoundException());

    UserStatus status = userStatusRepository.findByUserId(userId).orElseThrow(
        () -> new InvalidOperationException(DEFAULT_ERROR_MESSAGE)
    );

    status.updateLastOnline();

    userStatusRepository.save(status);

    return status;
  }

  @Override
  public void delete(String id) {
    userStatusRepository.deleteById(id);
  }
}
