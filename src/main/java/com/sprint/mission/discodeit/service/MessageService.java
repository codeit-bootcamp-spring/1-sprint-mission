package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.page.PageResponse;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface MessageService {

  MessageDto create(MessageCreateRequest messageCreateRequest, List<MultipartFile> attachments);

  PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant createAt,
      Pageable pageable);

  MessageDto find(UUID messageId);

  MessageDto update(UUID messageId, MessageUpdateRequest messageUpdateRequest);

  void delete(UUID messageId, UUID writerId);
}
