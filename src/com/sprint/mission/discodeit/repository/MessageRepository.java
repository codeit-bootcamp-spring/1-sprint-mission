package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.UUID;

public interface MessageRepository {
    Message createMessage(Message messageToCreate);
    Message findMessageById(UUID key);
    Message updateMessageById(UUID key, Message messageToUpdate);
    Message deleteMessageById(UUID key);
}
