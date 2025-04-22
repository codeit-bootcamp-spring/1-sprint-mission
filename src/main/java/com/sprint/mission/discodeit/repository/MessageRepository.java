package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  Slice<Message> findAllByChannelIdOrderByCreatedAtDesc(UUID channelId, Pageable pageable);

  List<Message> findAllByChannelId(UUID channelId);

  void deleteAllByChannelId(UUID channelId);

//  @Query("SELECT MAX(m.createdAt) FROM Message m WHERE m.channel.id = :channelId")
//  Instant findLastMessageTime(UUID channelId);
}
