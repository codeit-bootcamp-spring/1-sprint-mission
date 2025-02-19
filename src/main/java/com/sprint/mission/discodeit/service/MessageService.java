package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.message.*;

import java.util.Map;
import java.util.UUID;

public interface MessageService {
    MessageCreateResponse createMessage(MessageCreateRequest createRequest, MessageSendFileRequest... fileRequests);

    MessageResponse getMessageById(UUID messageId);

    Map<UUID, MessageResponse> findAllByChannelId(UUID channelId);

    MessageResponse updateMessage(MessageUpdateRequest updateRequest);

    void deleteMessage(UUID messageId);
}
