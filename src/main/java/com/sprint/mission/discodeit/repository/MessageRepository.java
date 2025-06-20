package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MessageRepository extends JpaRepository<Message, UUID> {

  List<Message> findByChannelId(UUID channelId); // Channel 에 포함된 메세지 찾기
  
  Page<Message> findByChannelId(UUID channelId, Pageable pageable);
}

