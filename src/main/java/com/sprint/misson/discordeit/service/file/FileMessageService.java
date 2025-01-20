package com.sprint.misson.discordeit.service.file;

import com.sprint.misson.discordeit.entity.Channel;
import com.sprint.misson.discordeit.entity.Message;
import com.sprint.misson.discordeit.entity.User;
import com.sprint.misson.discordeit.service.MessageService;

import java.util.List;

public class FileMessageService implements MessageService {
    @Override
    public Message createMessage(User user, String content, Channel channel) {
        return null;
    }

    @Override
    public List<Message> getMessages() {
        return List.of();
    }

    @Override
    public Message getMessageByUUID(String messageId) {
        return null;
    }

    @Override
    public List<Message> getMessageByContent(String content) {
        return List.of();
    }

    @Override
    public List<Message> getMessageBySender(User sender) {
        return List.of();
    }

    @Override
    public List<Message> getMessageByCreatedAt(Long createdAt) {
        return List.of();
    }

    @Override
    public List<Message> getMessagesByChannel(Channel channel) {
        return List.of();
    }

    @Override
    public Message updateMessage(String messageId, String content) {
        return null;
    }

    @Override
    public boolean deleteMessage(Message message) {
        return false;
    }
}
