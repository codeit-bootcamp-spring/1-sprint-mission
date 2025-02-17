package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public class FileMessageService implements MessageService {
    private final MessageRepository messageRepository;

    public FileMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public void messageSave(Message message) {
        messageRepository.createMessage(message.getId(),message);
    }

    @Override
    public Optional<Message> findMessage(UUID id) {
       return messageRepository.findById(id);
    }

    @Override
    public List<Message> findAllMessages() {
       return messageRepository.findAll();
    }

    @Override
    public void updateMessage(UUID id, String updateMessage) {
        validateFileMessageExits(id);
        messageRepository.updateMessage(id,updateMessage);
    }

    @Override
    public void deleteMessage(UUID id) {
        validateFileMessageExits(id);
        messageRepository.deleteMessage(id);
    }
    private void validateFileMessageExits(UUID uuid) {
        if (!messageRepository.findById(uuid).isPresent()) {
            throw new RuntimeException("해당 User가 존재하지 않습니다.");
        }
    }
}
