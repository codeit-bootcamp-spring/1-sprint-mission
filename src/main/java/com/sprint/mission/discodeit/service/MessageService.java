package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    Message createMessage(MessageDto messageDto);

    Message readMessage(UUID messageId);

    List<Message> readAll();

    void updateMessage(UUID messageId, String content);

    void deleteMessage(UUID messageId);
}
