package com.sprint.mission.discodeit.service.Interface;

import com.sprint.mission.discodeit.dto.binary.BinaryContentDto;
import com.sprint.mission.discodeit.dto.message.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequestDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Message;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface MessageService {

  MessageDto createMessage(CreateMessageRequestDto request
      , List<BinaryContentDto> binaryContentCreateRequests);

  Message getMessageById(UUID id);

  List<Message> getAllMessages();

  PageResponse<MessageDto> findAllByChannelId(UUID channelID, Instant cursor, int size);

  MessageDto updateMessage(UUID id, UpdateMessageRequestDto request);

  void deleteMessage(UUID id);

  void deleteByChannelId(UUID channelID);

  BinaryContentDto saveAttachment(MultipartFile multipartFile);
}
