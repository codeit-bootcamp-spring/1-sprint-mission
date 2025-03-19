package com.spirnt.mission.discodeit.repository;

import com.spirnt.mission.discodeit.enity.ReadStatus;
import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

  ReadStatus save(ReadStatus readStatus);

  void delete(ReadStatus readStatus);

  void deleteById(UUID id);

  void deleteByChannelId(UUID channelId);

  Optional<ReadStatus> findById(UUID id);


  List<ReadStatus> findAll();

  List<ReadStatus> findAllByUserId(UUID userId);

  List<ReadStatus> findAllByChannelId(UUID channelId);

  // 존재 검증
  boolean existsById(UUID id);

  boolean existsByUserIdAndChannelId(UUID userId, UUID channelId);

  @Transactional
  @Modifying
  @Query("UPDATE ReadStatus rs SET rs.lastReadAt = :newLastReadAt WHERE rs.id = :id")
  int updateById(@Param("id") UUID id, @Param("newLastReadAt") Instant newLastReadAt);
}
