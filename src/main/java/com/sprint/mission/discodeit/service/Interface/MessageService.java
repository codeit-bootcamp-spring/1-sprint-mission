package com.sprint.mission.discodeit.service.Interface;

import com.sprint.mission.discodeit.dto.message.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.UpdateMessageRequestDto;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    Message createMessage(CreateMessageRequestDto request);
    Optional<Message> getMessageById(UUID id);
    List<Message> getAllMessages();
    List<Message> findAllByChannelId(UUID channelID);
    Message updateMessage(UpdateMessageRequestDto request);
    void deleteMessage(UUID id);
    void deleteByChannelId(UUID channelID);
}
