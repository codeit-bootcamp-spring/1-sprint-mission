package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;

    @Override
    public void messageSave(Message message) {
        if (message.getContent().trim().isEmpty()) {
            log.info("메세지 내용을 입력해주세요.");
            return;
        }
        messageRepository.createMessage(message.getId(), message);
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
    public void updateMessage(UUID id,String updateMessage) {
        validateMessageExits(id);
        messageRepository.updateMessage(id, updateMessage);
    }


    @Override
    public void deleteMessage(UUID id) {
        validateMessageExits(id);
        messageRepository.deleteMessage(id);
    }
    private void validateMessageExits(UUID uuid) {
        if (!messageRepository.findById(uuid).isPresent()) {
            throw new RuntimeException("해당 User가 존재하지 않습니다.");
        }
    }
}
