package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface MessageService {

  MessageDto create(MessageCreateRequest messageCreateRequest, List<MultipartFile> attachments);

  List<MessageDto> findByChannel(UUID channelId);

  MessageDto update(UUID messageId, MessageUpdateRequest messageUpdateRequest);

  void delete(UUID messageId, UUID writerId);
}
