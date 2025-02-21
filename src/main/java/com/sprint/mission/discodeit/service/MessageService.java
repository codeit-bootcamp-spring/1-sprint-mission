package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(MessageCreateRequest messageCreateRequest, List<BinaryContentRequest> binaryContentRequests);
    MessageResponse find(UUID messageId);
    List<MessageResponse> findAllByChannelId(UUID channelId);
    MessageResponse getMessageInfo(Message message);
    Message update(MessageUpdateRequest messageUpdateRequest);
    void delete(UUID messageId);
}
