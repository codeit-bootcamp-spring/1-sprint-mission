package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.Message;

import java.util.List;
import java.util.Optional;

public interface MessageServiceV2 {
    Message createMessage(Message message, String channelId);
    Optional<Message> getMessageById(String messageId, String channelId);
    List<Message> getMessagesByChannel(String channelId);
    Message updateMessage(String channelId, String messageId, MessageUpdateDto updatedMessage);
    boolean deleteMessage(String messageId, String channelId);


}
