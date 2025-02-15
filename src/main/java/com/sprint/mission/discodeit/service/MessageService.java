package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageResponseDto create(MessageCreateRequestDto messageCreateRequestDto);
    Message find(UUID messageId);
    List<Message> findAll();
    MessageResponseDto getMessageInfo(Message message);
    void update(UUID messageId, String content);
    void delete(UUID messageId);
}
