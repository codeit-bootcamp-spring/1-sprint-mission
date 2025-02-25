package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageDTO;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;

public interface MessageService {
    MessageDTO createMessage(MessageDTO messageDTO);  // create -> createMessage로 변경
    MessageDTO updateMessage(String id, MessageDTO messageDTO);
    void deleteMessage(String id);
    List<MessageDTO> getChannelMessages(String channelId);
    List<MessageDTO> findAllByChannelId(String channelId);
    List<MessageDTO> findAll();
}