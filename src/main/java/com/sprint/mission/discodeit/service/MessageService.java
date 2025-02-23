package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageResponse;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(MessageCreateRequest messageCreateRequest, List<BinaryContentRequest> binaryContentRequests);
    Message find(UUID messageId);
    List<Message> findAllByChannelId(UUID channelId);
    List<Message> findAllByAuthorId(UUID authorId);
    Message update(UUID messageId, MessageUpdateRequest messageUpdateRequest);
    void delete(UUID messageId);
}
