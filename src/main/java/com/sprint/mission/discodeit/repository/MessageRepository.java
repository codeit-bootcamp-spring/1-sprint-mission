package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;

import java.util.UUID;

public interface MessageRepository {
    Message createMessage(Message messageInfoToCreate);
    Message findMessageById(UUID key);
    Message updateMessageById(UUID key, Message messageInfoToUpdate);
    Message deleteMessageById(UUID key);
}
