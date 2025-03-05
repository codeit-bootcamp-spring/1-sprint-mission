package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.MessageResponse;
import com.sprint.mission.discodeit.dto.MessageUpdateRequest;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageResponse create(MessageCreateRequest messageCreateRequest); // ✅ 반환 타입 변경 (void → MessageResponse)
    void update(UUID messageId, MessageUpdateRequest messageUpdateRequest);
    void delete(UUID messageId);
    List<MessageResponse> readAllByChannel(UUID channelId);
    List<MessageResponse> readAll();
}
