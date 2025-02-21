package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageDTO;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;

public interface MessageService {
    Message create(MessageDTO messageDTO);
    Message find(String messageId);
    Message update(String messageId, MessageDTO messageDTO);
    void delete(String messageId);
    List<Message> findAllByChannelId(String channelId);
}