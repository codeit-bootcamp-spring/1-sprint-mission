package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(String content, UUID userId, UUID channelId);
    Message findById(UUID id);
    List<Message> findAll();
    Message update(UUID id, String newContent);
    void delete(UUID id);
}
