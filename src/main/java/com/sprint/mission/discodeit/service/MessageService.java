package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
  Message create(MessageCreateRequest messageCreateRequest, List<BinaryContentCreateRequest> binaryContentCreateRequests);

  Optional<Message> find(UUID id);

  List<Message> findAllByChannelId(UUID channelId);

  Message update(UUID messageId, UUID requesterId, MessageUpdateRequest request);

  void delete(UUID messageId, UUID requesterId);
}