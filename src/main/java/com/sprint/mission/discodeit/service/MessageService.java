package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateDTO;
import com.sprint.mission.discodeit.dto.message.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDTO;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Pageable;

public interface MessageService {

  MessageDto createMessage(MessageCreateDTO messageCreateDTO,
      List<BinaryContentCreateDTO> attachmentRequests);

  MessageDto findById(UUID id);

  PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant createdAt, Pageable pageable);


  MessageDto update(UUID id, MessageUpdateDTO messageUpdateDTO);

  void deleteMessage(UUID msgID);


}
