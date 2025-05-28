package com.sprint.mission.discodeit.service.facade.message;

import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

public interface MessageFacade {

  MessageResponseDto createMessage(CreateMessageDto messageDto, List<MultipartFile> files);

  MessageResponseDto findMessageById(String id);

  PageResponse<MessageResponseDto> findMessagesByChannel(String channelId, Instant nextCursor,
      Pageable pageable);

  MessageResponseDto updateMessage(String messageId, MessageUpdateDto messageDto,
      UserDetails userDetails);

  void deleteMessage(String messageId,
      UserDetails userDetails);
}
