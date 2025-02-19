package com.sprint.mission.discodeit.application.service.interfaces;

import com.sprint.mission.discodeit.application.dto.message.CreateMessageRequestDto;
import com.sprint.mission.discodeit.application.dto.message.DeleteMessageRequestDto;
import com.sprint.mission.discodeit.application.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.application.dto.message.UpdateMessageContentRequestDto;
import com.sprint.mission.discodeit.domain.message.Message;
import java.util.UUID;

public interface MessageService {

    MessageResponseDto createMessage(CreateMessageRequestDto requestDto);

    void updateMessage(UUID userId, UpdateMessageContentRequestDto requestDto);

    void deleteMessage(UUID userId, DeleteMessageRequestDto requestDto);

    Message findOneByMessageIdOrThrow(UUID uuid);

}
