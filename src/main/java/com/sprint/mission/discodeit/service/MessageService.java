package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageDto;

import java.util.List;

public interface MessageService {
    MessageDto createMessage(MessageDto messageDTO);  // create -> createMessage로 변경
    MessageDto updateMessage(String id, MessageDto messageDTO);
    void deleteMessage(String id);
    List<MessageDto> getChannelMessages(String channelId);
    List<MessageDto> findAllByChannelId(String channelId);
    List<MessageDto> findAll();
}