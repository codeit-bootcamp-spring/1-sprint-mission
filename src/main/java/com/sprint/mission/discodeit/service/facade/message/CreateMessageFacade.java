package com.sprint.mission.discodeit.service.facade.message;

import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CreateMessageFacade {
  MessageResponseDto createMessage(CreateMessageDto messageDto, List<MultipartFile> files);
}
