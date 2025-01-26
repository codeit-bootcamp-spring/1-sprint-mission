package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.service.MessageService;

import java.util.*;

public class FileMessageService implements MessageService {
    private final MessageRepository messageRepository=new FileMessageRepository();

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
        if(messageRepository.findById(id).isPresent()) {
          messageRepository.updateMessage(id,updateMessage);
        }else {
            throw new RuntimeException("해당 User가 존재하지 않습니다.");
        }
    }

    @Override
    public void deleteMessage(UUID id) {
        if (messageRepository.findById(id).isPresent()) {
            messageRepository.deleteMessage(id);
        }else {
            throw new RuntimeException("해당 User가 존재하지 않습니다.");
        }
    }
}
