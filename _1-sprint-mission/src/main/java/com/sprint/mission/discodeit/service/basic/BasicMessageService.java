package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;

    public BasicMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public Message create(String content, UUID channelId, UUID authorId) {
        Message message = new Message(content, channelId, authorId);
        return messageRepository.create(message);
    }

    @Override
    public Message find(UUID messageId) {
        Message message = messageRepository.findById(messageId);
        if (message == null) throw new NoSuchElementException("Message not found with ID: " + messageId);
        return message;
    }

    @Override
    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    @Override
    public Message update(UUID messageId, String newContent) {
        Message message = find(messageId);
        message.updateContent(newContent);
        return messageRepository.create(message);
    }

    @Override
    public void delete(UUID messageId) {
        messageRepository.delete(messageId);
    }
}
