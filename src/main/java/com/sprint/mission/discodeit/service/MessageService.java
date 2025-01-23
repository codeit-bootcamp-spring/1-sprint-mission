package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(String content, UUID authorId, UUID channelId);

    Message findById(UUID contentId);

    List<Message> findAll();

    Message update(UUID contentId, String content);

    void delete(UUID contentId);
}
