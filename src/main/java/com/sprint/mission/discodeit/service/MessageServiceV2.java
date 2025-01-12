package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.entity.ChatChannel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.MessageValidationException;

import java.util.List;
import java.util.Optional;

public interface MessageServiceV2 {
    Message createMessage(String userId, Message message, ChatChannel chatChannel) throws MessageValidationException;
    Optional<Message> getMessageById(String messageId, ChatChannel chatChannel);
    List<Message> getMessagesByChannel(ChatChannel chatChannel);
    Message updateMessage(ChatChannel chatChannel, String messageId, MessageUpdateDto updatedMessage);
    boolean deleteMessage(String messageId, ChatChannel chatChannel);


}
