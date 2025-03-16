package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {

  MessageDto create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentDto> binaryContentCreateRequests);

  Optional<Message> find(UUID id);

  PageResponse<Message> findAllByChannelId(UUID channelId, int page);

  MessageDto update(UUID messageId, UUID requesterId, MessageUpdateRequest request);

  void delete(UUID messageId, UUID requesterId);
}