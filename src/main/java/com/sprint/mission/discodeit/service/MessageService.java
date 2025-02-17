package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.entity.BinaryContent;
import com.sprint.mission.discodeit.dto.entity.Message;
import com.sprint.mission.discodeit.dto.form.MessageWithContents;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    void messageSave(Message message);
    void messageSaveWithContents(MessageWithContents message, BinaryContent binaryContent);
    Optional<Message> findMessage(UUID id);
    List<Message> findAllMessages();
    Optional<Message> findAllByChannelId(UUID channelId);
    void updateMessage(UUID id,String updateMessage);
    void deleteMessage(UUID id);
}
