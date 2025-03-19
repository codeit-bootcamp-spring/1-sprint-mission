package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.PageResponse;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDTO;
import com.sprint.mission.discodeit.entity.Message;


import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface MessageService {


  MessageDto create(MessageCreateDTO dto,
      List<BinaryContentCreateRequest> binaryContentCreateRequests);

  MessageDto find(UUID id);

  PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant cursor, Pageable pageable);

  MessageDto update(UUID id, MessageUpdateDTO messageUpdateDTO);

  void delete(UUID id);
}