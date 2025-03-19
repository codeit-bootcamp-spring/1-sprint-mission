package com.spirnt.mission.discodeit.repository;

import com.spirnt.mission.discodeit.enity.Channel;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChannelRepository extends JpaRepository<Channel, UUID> {

  // 데이터베이스 저장
  Channel save(Channel channel);

  void deleteById(UUID channelId);

  Optional<Channel> findById(UUID channelId);

  List<Channel> findAll();

  // 존재 검증
  boolean existsById(UUID channelId);

  @Transactional
  @Modifying
  @Query("UPDATE Channel c SET c.name = :newName, c.description = :newDescription WHERE c.id = :id")
  int updateById(@Param("id") UUID id, @Param("newName") String newName,
      @Param("newDescription") String newDescription);
}
