package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageRequest;
import com.sprint.mission.discodeit.dto.MessageResponse;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;

public interface MessageService extends CRUDService<MessageRequest, MessageResponse> {
    List<MessageResponse> readAll(UUID id);
}
