package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.domain.entity.BinaryContent;
import com.sprint.mission.discodeit.domain.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    Message messageSave(UUID id,Message message);
    Message messageSaveWithContents(UUID senderId,Message message, List<BinaryContent> files);
    Optional<Message> findMessage(UUID id);
    List<Message> findAllMessages();
    Optional<Message> findAllByChannelId(UUID channelId);
    void updateMessage(UUID id,String updateMessage);
    void deleteMessage(UUID id);
}
