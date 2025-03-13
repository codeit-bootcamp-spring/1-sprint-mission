package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;

import com.sprint.mission.discodeit.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

  @Query("select m from ReadStatus m join fetch m.user u where m.user.id =: userId")
  List<ReadStatus> findAllByUserId(@Param("userId") UUID userId);

  @Query("select m from ReadStatus m join m.channel where m.channel.id = :channelId")
  List<ReadStatus> findReadStatusesByChannelId(@Param("channelId") UUID channelId);

  boolean existsById(UUID id);

  void deleteById(UUID id);

  void deleteAllByChannelId(UUID channelId);
}
