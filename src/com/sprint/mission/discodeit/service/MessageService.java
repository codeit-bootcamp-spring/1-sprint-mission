package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Message;

import java.util.UUID;

public interface MessageService {
    Message createMessage(Message messageInfoToCreate);
    Message findMessageById(UUID key);
    Message updateMessageById(UUID key, Message messageInfoToUpdate);
    Message deleteMessageById(UUID key);
}
