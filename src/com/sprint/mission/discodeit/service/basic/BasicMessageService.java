package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.UUID;

public class BasicMessageService {
    public static Message setupMessage(MessageService messageService, Message messageInfoToCreate) {
        return messageService.createMessage(messageInfoToCreate);
    }

    public static Message updateMessage(MessageService messageService, Message messageInfoToUpdate) {
        return messageService.updateMessageById(messageInfoToUpdate.getId(), messageInfoToUpdate);
    }

    public static Message searchMessage(MessageService messageService, UUID key) {
        return messageService.findMessageById(key);
    }

    public static Message removeMessage(MessageService messageService, UUID key) {
        return messageService.deleteMessageById(key);
    }
}
