package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(String content, UUID channelId, UUID writerId);
    Message findById(UUID messageId);
    Message findByChannel(UUID channelId);
    Message findByUser(UUID userId);
    List<Message> findAll();
    Message update(UUID messageId, UUID writerId, String newContent);
    void delete(UUID messageId, UUID writerId);
}
