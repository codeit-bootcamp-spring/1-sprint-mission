package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDTO;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.UUID;
import org.springframework.web.multipart.MultipartFile;

public interface MessageService {

  Message create(MessageCreateRequest messageCreateRequest, List<MultipartFile> attachments);

  MessageDTO findById(UUID messageId);

  List<MessageDTO> findByChannel(UUID channelId);

  List<MessageDTO> findByUser(UUID userId);

  Message update(UUID messageId, MessageUpdateRequest messageUpdateRequest);

  void delete(UUID messageId, UUID writerId);
}
