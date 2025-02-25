package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.domain.Message;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(UUID writerID, CreateMessageRequest request);

    List<Message> findAllByChannelId(UUID channelID);

    List<Message> getAllMessage();

    Message updateMessageContent(UUID writerID, UpdateMessageRequest request);

    Message deleteMessage(UUID messageID);
}
