package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.util.Map;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserStatusRepository extends JpaRepository<UserStatus, UUID> {


  UserStatus findByUserId(UUID userId);

  Boolean existsByUserId(UUID userId);


  //TODO Sprint 3 null 예외 사항 처리하기
  @Override
  public UserStatus findByUserId(UUID userId) {
    if (userId == null) {
      throw new IllegalArgumentException("Id is null.");
    }

    Map<UUID, UserStatus> paramMap = userStatusMap;

    return userStatusMap.values().stream()
        .filter(userStatus -> userStatus.getUserId().equals(userId))
        .findFirst()
        .orElseThrow(() -> new NullPointerException("No found"));

  }

  @Override
  public Boolean existsByUserId(UUID userId) {
    if (userId == null) {
      throw new IllegalArgumentException("Id is null.");
    }
    return userStatusMap.values().stream()
        .anyMatch(userStatus -> userStatus.getUserId().equals(userId));
  }


}
