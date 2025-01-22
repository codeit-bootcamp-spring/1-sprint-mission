package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.Map;
import java.util.UUID;

public interface MessageRepository {
    // 저장
    void saveMessage(Message message);

    // 읽기
    Message findMessageById(UUID id);
    Map<UUID, Message> findAllMessages();

    // 삭제
    void deleteMessageById(UUID id);
    void deleteAllMessages();


}
