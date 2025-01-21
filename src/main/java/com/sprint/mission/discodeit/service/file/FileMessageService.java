package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.factory.BaseEntityFactory;
import com.sprint.mission.discodeit.factory.EntityFactory;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FileMessageService implements MessageService, FileService<Message> {
    private static final String MESSAGE_PATH = "message.ser";
    static EntityFactory ef;
    private Map<UUID, Message> messageList = new HashMap<>();

    public FileMessageService(EntityFactory ef) {
        FileMessageService.ef = ef;
    }

    public FileMessageService() {
        ef = BaseEntityFactory.getInstance();
    }

    @Override
    public Message createMessage(String title, String content, User sender, User receiver) {
        Message message = ef.createMessage(title, content, sender, receiver);
        messageList.put(message.getMessageId(), message);

        save(MESSAGE_PATH, messageList);

        return message;
    }

    @Override
    public Message getMessageById(UUID messageId) {
        Message message = messageList.get(messageId);

        return message;
    }

    @Override
    public Map<UUID, Message> getAllMessages() {
        return load(MESSAGE_PATH, messageList);
    }

    @Override
    public Message updateMessage(UUID messageId, String newTitle, String newContent) {
        Message findMessage = getMessageById(messageId);

        findMessage.updateMessageTitle(newTitle);
        findMessage.updateMessageContent(newContent);

        save(MESSAGE_PATH, messageList);

        return findMessage;
    }

    @Override
    public void deleteMessage(UUID messageId) {
        Map<UUID, Message> load = load(MESSAGE_PATH, messageList);

        load.remove(messageId);

        save(MESSAGE_PATH, load);
    }
}
