package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageDTO;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    Message create(MessageCreateRequest messageCreateRequest, List<BinaryContentCreateRequest> binaryContentCreateRequests);
    MessageDTO findById(UUID messageId);
    List<MessageDTO> findByChannel(UUID channelId);
    List<MessageDTO> findByUser(UUID userId);
    Message update(UUID messageId, UUID channelId, UUID writerId, MessageUpdateRequest messageUpdateRequest);
    void delete(UUID messageId, UUID writerId);
}
