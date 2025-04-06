package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageDto;

import java.util.List;
import java.util.UUID;

public interface MessageService {
    MessageDto createMessage(MessageDto messageDTO);  // create -> createMessage로 변경
    MessageDto updateMessage(UUID id, MessageDto messageDTO);
    void deleteMessage(UUID id);
    List<MessageDto> getChannelMessages(UUID channelId);
    List<MessageDto> findAllByChannelId(UUID channelId);
    List<MessageDto> findAll();
    MessageDto getMessageById(UUID id);
}