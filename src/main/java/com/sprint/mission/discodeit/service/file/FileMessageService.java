package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.MessageDTO;
import com.sprint.mission.discodeit.dto.MessageUpdateDTO;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service("fileMessageService")
@RequiredArgsConstructor
public class FileMessageService implements MessageService {

    private final @Qualifier("fileMessageRepository") MessageRepository messageRepository;

    @Override
    public MessageDTO create(MessageCreateDTO messageCreateDTO) {
        Message message = new Message(
                UUID.randomUUID(),
                messageCreateDTO.getContent(),
                messageCreateDTO.getSenderId(),
                messageCreateDTO.getChannelId(),
                Instant.now()
        );
        messageRepository.save(message);
        return new MessageDTO(
                message.getId(),
                message.getContent(),
                message.getSenderId(),
                message.getChannelId(),
                message.getCreatedAt()
        );
    }

    @Override
    public void update(UUID messageId, MessageUpdateDTO messageUpdateDTO) {
        Optional<Message> optionalMessage = messageRepository.findById(messageId);
        if (optionalMessage.isPresent()) {
            Message message = optionalMessage.get();
            message.setContent(messageUpdateDTO.getContent());
            messageRepository.save(message);
        } else {
            throw new IllegalArgumentException("해당 ID의 메시지를 찾을 수 없습니다.");
        }
    }

    @Override
    public void delete(UUID messageId) {
        messageRepository.deleteById(messageId);
    }

    @Override
    public List<MessageDTO> readAllByChannel(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId).stream()
                .map(message -> new MessageDTO(
                        message.getId(),
                        message.getContent(),
                        message.getSenderId(),
                        message.getChannelId(),
                        message.getCreatedAt()
                ))
                .toList();
    }

    @Override
    public List<MessageDTO> readAll() {
        return messageRepository.findAll().stream()
                .map(message -> new MessageDTO(
                        message.getId(),
                        message.getContent(),
                        message.getSenderId(),
                        message.getChannelId(),
                        message.getCreatedAt()
                ))
                .toList();
    }
}
