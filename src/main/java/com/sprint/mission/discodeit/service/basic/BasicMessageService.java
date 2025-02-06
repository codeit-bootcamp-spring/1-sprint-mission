package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.Message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.Message.MessageDto;
import com.sprint.mission.discodeit.dto.Message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.notfound.NotfoundIdException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public MessageDto create(MessageCreateRequest request, UUID channelId) {
        Message message = new Message(request.content(), request.userId(), channelId, request.attachmentIds());
        messageRepository.save(message);

        if (request.attachmentIds() != null && !request.attachmentIds().isEmpty()) {
            request.attachmentIds().forEach(attachmentId -> {
                BinaryContent binaryContent = binaryContentRepository.findByContentId(attachmentId);
                if (binaryContent != null) {
                    message.getAttachmentIds().add(attachmentId);
                }
            });
        }

        messageRepository.save(message);
        return new MessageDto(message.getId(), message.getContent(), message.getCreatedAt(), message.getAttachmentIds());
    }

    @Override
    public List<MessageDto> findAllByChannelId(UUID channelId) {
        List<Message> messages = messageRepository.findByChannelId(channelId);
        return messages.stream()
                .map(message -> new MessageDto(message.getId(), message.getContent(), message.getCreatedAt(), message.getAttachmentIds()))
                .collect(Collectors.toList());
    }

    @Override
    public MessageDto update(MessageUpdateRequest request) {
        Message message = messageRepository.findByMessageId(request.messageId());
        if (message == null) {
            throw new NotfoundIdException("Message not found");
        }

        message.update(request.newContent(), request.attachmentIds());
        request.attachmentIds().forEach(attachmentId -> {
            BinaryContent binaryContent = binaryContentRepository.findByContentId(attachmentId);
            if (binaryContent != null) {
                message.getAttachmentIds().add(attachmentId);
            }
        });

        messageRepository.save(message);
        return new MessageDto(message.getId(), message.getContent(), message.getCreatedAt(), message.getAttachmentIds());
    }

    @Override
    public void delete(UUID messageId) {
        Message message = messageRepository.findByMessageId(messageId);
        if (message != null) {
            message.getAttachmentIds().forEach(binaryContentRepository::deleteByContentId);
            messageRepository.deleteByMessageId(messageId);
        } else {
            throw new NotfoundIdException("Message not found");
        }
    }
}
