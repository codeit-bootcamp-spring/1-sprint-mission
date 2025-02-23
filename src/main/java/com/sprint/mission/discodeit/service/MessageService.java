package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.MessageRequest;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    Message createMessage(MessageRequest messageRequest);

    Message readMessage(UUID messageId);

    List<Message> readAllByChannelId(UUID channelId);

    void updateMessage(UUID messageId, String content);

    void deleteMessage(UUID messageId);
}
