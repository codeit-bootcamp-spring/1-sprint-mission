package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.message.Message;

import java.util.Map;
import java.util.UUID;

public interface MessageRepository {
    // 메세지 저장
    Message saveMessage(Message message);

    // 메세지 삭제
    void removeMessageById(UUID messageId);

    // 메세지 조회
    Message findMessageById(UUID messageId);

    // 모든 메세지 조회
    Map<UUID, Message> findAllMessage();
}
