package com.sprint.mission.discodeit.repository.jpa.userStatus;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserStatusRepositoryImpl implements UserStatusRepository {

  private final UserStatusJpaRepository userStatusRepository;

  @Override
  public UserStatus saveUserStatus(UserStatus userStatus) {
    return userStatusRepository.save(userStatus);
  }

  @Override
  public Optional<UserStatus> findUserStatusById(UUID userStatusId) {
    return userStatusRepository.findById(userStatusId);
  }

  @Override
  public Optional<UserStatus> findUserStatusByUser(User user) {
    return userStatusRepository.findUserStatusByUser(user);
  }

  @Override
  public List<UserStatus> findAllUserStatuses() {
    return userStatusRepository.findAll();
  }

  @Override
  public void removeUserStatus(UUID userStatusId) {
    userStatusRepository.deleteById(userStatusId);
  }
}
