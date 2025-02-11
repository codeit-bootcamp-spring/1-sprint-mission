package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BasicMessageService implements MessageService {
    private static final Logger log = LoggerFactory.getLogger(BasicMessageService.class);
    private final MessageRepository messageRepository;

    public BasicMessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @Override
    public MessageDto createMessage(MessageDto messageDto) {
        log.info("메시지를 생성합니다");
        Message message = new Message(
                Instant.now(),
                messageDto.getContent(),
                messageDto.getChannelId(),
                messageDto.getAuthorId()
        );
        messageRepository.save(message);
        return new MessageDto(
                message.getId(),
                message.getContent(),
                message.getChannelId(),
                message.getAuthorId()
        );
    }

    @Override
    public MessageDto findMessageById(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("해당 ID로 메시지를 찾을 수 없습니다: " + messageId));
        return new MessageDto(
                message.getId(),
                message.getContent(),
                message.getChannelId(),
                message.getAuthorId()
        );
    }

    @Override
    public List<MessageDto> findMessagesByChannelId(UUID channelId) {
        List<Message> messages = messageRepository.findAllByChannelId(channelId);
        return messages.stream()
                .map(msg -> new MessageDto(
                        msg.getId(),
                        msg.getContent(),
                        msg.getChannelId(),
                        msg.getAuthorId()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public MessageDto updateMessage(UUID messageId, MessageDto messageDto) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new RuntimeException("해당 ID로 메시지를 찾을 수 없습니다: " + messageId));
        message.update(messageDto.getContent());
        messageRepository.save(message);
        return new MessageDto(
                message.getId(),
                message.getContent(),
                message.getChannelId(),
                message.getAuthorId()
        );
    }

    @Override
    public void deleteMessage(UUID messageId) {
        log.info("메시지를 삭제합니다: {}", messageId);
        messageRepository.deleteById(messageId);
    }
}
