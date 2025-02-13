package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
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

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {
    private final MessageRepository messageRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final ValidateMessage validateMessage;

    @Override
    public MessageDto create(MessageCreateRequest messageCreateRequest, List<BinaryContentCreateRequest> binaryContentCreateRequests) {
        //validateMessage.validateMessage(request.message(), request.userId(), request.channelId());
        UUID channelId = messageCreateRequest.channelId();
        UUID authorId = messageCreateRequest.authorId();

        List<UUID> attachmentIds = binaryContentCreateRequests.stream()
                .map(request -> {
                    String fileName = request.fileName();
                    String contentType = request.contentType();
                    byte[] bytes = request.bytes();

                    BinaryContent binaryContent = new BinaryContent(fileName, (long)bytes.length, contentType, bytes);
                    BinaryContent createdbinaryContent = binaryContentRepository.save(binaryContent);
                    return  createdbinaryContent.getId();
                })
                .toList();

        String content = messageCreateRequest.message();
        Message message = new Message(content, channelId, authorId, attachmentIds);
        Message createdMessage = messageRepository.save(message);

        return changeToDto(createdMessage);
    }

    @Override
    public MessageDto findById(UUID messageId){
        return messageRepository.findById(messageId)
                .map(this::changeToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found."));
    }

    @Override
    public List<MessageDto> findAllByChannelId(UUID channelId) {
        return messageRepository.findAllByChannelId(channelId).stream()
                .map(this::changeToDto)
                .toList();
    }

    @Override
    public MessageDto update(UUID messageId, MessageUpdateRequest request) {
        // validateMessage.validateMessage(request.message(), request.userId(), request.channelId());

        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found."));

        message.update(request.newMessage());
        Message updatedMessage = messageRepository.save(message);
        return changeToDto(updatedMessage);
    }

    @Override
    public void delete(UUID messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new ResourceNotFoundException("Message not found."));
        message.getAttachmentIds()
                .forEach(binaryContentRepository::deleteById);
        messageRepository.deleteById(messageId);
    }

    private MessageDto changeToDto(Message message) {
        return new MessageDto(
                message.getId(),
                message.getAuthorId(),
                message.getChannelId(),
                message.getMessage(),
                message.getAttachmentIds(),
                message.getCreatedAt(),
                message.getUpdatedAt()
        );
    }
}
