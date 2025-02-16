package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequest;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    MessageResponse createMessage(CreateMessageRequest request);

    List<MessageResponse> getMessages();

    List<MessageResponse> getMessagesByChannel(UUID ChannelID);

    Optional<MessageResponse> getMessage(UUID uuid);

    Optional<MessageResponse> updateMessage(UpdateMessageRequest request);

    void deleteMessage(UUID uuid);
}
