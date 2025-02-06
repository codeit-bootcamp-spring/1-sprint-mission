package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.Message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.Message.MessageDto;
import com.sprint.mission.discodeit.dto.Message.MessageUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageDto create(MessageCreateRequest request, UUID channelId);
    List<MessageDto> findAllByChannelId(UUID channelId);
    MessageDto update(MessageUpdateRequest request);
    void delete(UUID messageId);
}
