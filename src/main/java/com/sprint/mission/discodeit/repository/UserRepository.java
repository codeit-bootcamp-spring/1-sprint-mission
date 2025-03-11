package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

  Boolean existByUserId(UUID userId);

  boolean existsByUsername(String username);

  List<UUID> findAllUserIdByChannelId(UUID uuid); //채널에 참여하는 유저 id 리스트 반환


  @Override
  public List<UUID> findAllUserIdByChannelId(UUID uuid) {
    //채널아이디 대상, 속해있는 유저 반환
    return List.of();
  }

  @Override
  public Boolean existByUserId(UUID userId) {
    return load().values().stream()
        .anyMatch(user -> user.getId().equals(userId));
  }

  @Override
  public boolean existsByUsername(String username) {
    return load().values().stream()
        .anyMatch(user -> user.getUserName().equals(username));
  }


}
