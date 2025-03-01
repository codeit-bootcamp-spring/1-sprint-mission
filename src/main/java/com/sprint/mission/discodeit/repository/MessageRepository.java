package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {

  // 저장
  Message saveMessage(Message message);

  // 읽기
  Optional<Message> findMessageById(UUID id);

  Collection<Message> findAllMessages();

  List<Message> findMessagesByChannelId(UUID channelId); // Channel에 포함된 메세지 찾기

  // 삭제
  void deleteMessageById(UUID id);

}
