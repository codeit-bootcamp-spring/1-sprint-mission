package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.ReadStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

  ReadStatus save(ReadStatus readStatus);

  @EntityGraph(attributePaths = {"user", "channel"})
  Optional<ReadStatus> findById(UUID id);

  @EntityGraph(attributePaths = {"user", "channel"})
  List<ReadStatus> findAllByUserId(UUID userId);

  @EntityGraph(attributePaths = {"user", "channel"})
  List<ReadStatus> findAllByChannelId(UUID channelId);

  boolean existsById(UUID id);

  void deleteById(UUID id);

  void deleteAllByChannelId(UUID channelId);
}
