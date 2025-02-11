package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.validation.ValidateMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final ValidateMessage validateMessage;

    @Override
    public MessageDto create(MessageCreateRequest request) {
        validateMessage.validateMessage(request.message(), request.userId(), request.channelId());

        Message message = new Message(request.message(), request.userId(), request.channelId(), request.content());
        messageRepository.save(message);
        if (request.content() != null) {
            for (byte[] content : request.content()){
                BinaryContent binaryContent = new BinaryContent(message.getAuthorId(), message.getId(), content);
                binaryContentRepository.save(binaryContent);
            }
        }
        return changeToDto(message);
    }

    @Override
    public List<MessageDto> findAllByChannelId(UUID channelId) {
        List<Message> messages = messageRepository.findByChannelId(channelId);
        return messages.stream()
                .map(BasicMessageService::changeToDto)
                .collect(Collectors.toList());
    }

    @Override
    public MessageDto update(MessageUpdateRequest request) {
        validateMessage.validateMessage(request.message(), request.userId(), request.channelId());
        Message message = messageRepository.findByMessageId(request.messageId())
                .orElseThrow(() -> new ResourceNotFoundException("Message not found."));

        // BinaryContent update
        binaryContentRepository.deleteByMessageId(request.messageId());
        if (request.content() != null) {
            for (byte[] content : request.content()){
                BinaryContent binaryContent = new BinaryContent(message.getAuthorId(), message.getId(), content);
                binaryContentRepository.save(binaryContent);
            }
        }
        message.update(request.message(), request.content());
        messageRepository.save(message);
        return changeToDto(message);
    }

    @Override
    public void delete(UUID messageId) {
        binaryContentRepository.deleteByMessageId(messageId);
        messageRepository.deleteByMessageId(messageId);
    }

    private static MessageDto changeToDto(Message message) {
        return new MessageDto(
                message.getId(),
                message.getAuthorId(),
                message.getChannelId(),
                message.getMessage(),
                message.getContent(),
                message.getCreatedAt(),
                message.getUpdatedAt()
        );
    }
}
