package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.MessageDTO;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    @Override
    public Message createMessage(MessageDTO messageDTO) {
        Message message = new Message(messageDTO);
        messageRepository.save(message);
        return null;
    }

    @Override
    public Message findById(UUID messageId) {
        return messageRepository.findById(messageId).orElseThrow(() -> new NoSuchElementException("MessageId : " + messageId + "를 찾을 수 없습니다."));
    }

    @Override
    public List<Message> findAllMessage() {
        Map<UUID, Message> data = messageRepository.findAll();
        return data.values().stream().sorted(Comparator.comparing(message -> message.getCreatedAt())).collect(Collectors.toList());
    }

    @Override
    public void update(UUID messageId, MessageDTO messageDTO) {
        Message message = messageRepository.findById(messageId).orElseThrow(()-> new NoSuchElementException("MessageId : " + messageId + "를 찾을 수 없습니다."));
        message.update(messageDTO);
        messageRepository.save(message);
    }

    @Override
    public void delete(UUID messageId) {
        messageRepository.delete(messageId);

    }

    @Override
    public void deleteInChannel(UUID channelId) {
        Map<UUID, Message> data = messageRepository.findAll();
        if(data == null) return;
        data.values().stream().forEach(message -> {
            if(message.getChannelId().equals(channelId)) {
                messageRepository.delete(message.getId());
            }
        });
    }
}
