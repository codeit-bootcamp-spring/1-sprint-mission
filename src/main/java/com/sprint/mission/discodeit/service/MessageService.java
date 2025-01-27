package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MessageService {
    Message createMessage(Message message);
    Optional<Message> readMessage(UUID existMessageId);
    List<Message> readAllMessages();
    Message updateByAuthor(UUID existUserId, Message updateMessage);
    boolean deleteMessage(UUID messageId);
}