package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, UUID> {

  boolean existsById(UUID userId);

  boolean existsByUsername(String username);


  boolean existsByEmail(String email);

//  //채널에 참여하는 유저 id 리스트 반환
//  List<UUID> findDistinctUserIdByChannelId(UUID channelId);

}
