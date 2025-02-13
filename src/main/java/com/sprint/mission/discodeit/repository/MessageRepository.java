package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public interface MessageRepository {
    // 저장
    void saveMessage(Message message);

    // 읽기
    Optional<Message> findMessageById(UUID id);
    Collection<Message> findAllMessages();

    Collection<Message> findMessagesByChannelId(UUID channelId); // Channel에 포함된 메세지 찾기

    // 삭제
    void deleteMessageById(UUID id);

}
