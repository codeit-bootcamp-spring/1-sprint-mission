package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.*;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message createMessage(MessageCreateRequest messageCreateRequest, List<BinaryContentCreateRequest> binaryContentCreateRequests);
    Message readMessageById(UUID messageId);
    List<Message> readAllByChannelId(UUID channelId);
    Message updateMessageField(UUID messageId, MessageUpdateRequest request);
    void deleteMessageById(UUID messageId);



}
