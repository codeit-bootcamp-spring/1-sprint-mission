package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.BaseChannel;
import com.sprint.mission.discodeit.entity.ChatChannel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.MessageValidationException;

import java.util.List;
import java.util.Optional;

public interface MessageServiceV2<T extends BaseChannel> {
    Message createMessage(String userId, Message message, T channel) throws MessageValidationException;
    Optional<Message> getMessageById(String messageId, T channel);
    List<Message> getMessagesByChannel(T channel);
    Message updateMessage(T channel, String messageId, MessageUpdateDto updatedMessage);
    boolean deleteMessage(String messageId, T channel);

}
