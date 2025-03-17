package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageDto create(MessageCreateRequest messageCreateRequest, List<BinaryContentRequest> binaryContentRequests);
    MessageDto find(UUID messageId);
    List<MessageDto> findAllByChannelId(UUID channelId);
    List<MessageDto> findAllByAuthorId(UUID authorId);
    MessageDto update(UUID messageId, MessageUpdateRequest request);
    void delete(UUID messageId);
}
