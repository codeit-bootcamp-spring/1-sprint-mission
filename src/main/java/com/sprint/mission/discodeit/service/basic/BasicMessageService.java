package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.UUID;

public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;

    public BasicMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void createMessage(Message message) {
        messageRepository.createMessage(message);
    }

    @Override
    public Message getMessage(UUID id) {
        return messageRepository.getMessage(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 메시지를 찾을 수 없습니다: " + id));
    }

    @Override
    public List<Message> getAllMessages() {
        return messageRepository.getAllMessages();
    }

    @Override
    public void updateMessage(UUID id, String content) {
        Message existingMessage = messageRepository.getMessage(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 메시지를 찾을 수 없습니다: " + id));
        existingMessage.update(content);
        messageRepository.updateMessage(id, content);
    }

    @Override
    public void deleteMessage(UUID id) {
        messageRepository.getMessage(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 ID의 메시지를 찾을 수 없습니다: " + id));
        messageRepository.deleteMessage(id);
    }
}
