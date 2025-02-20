package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.validation.MessageValidator;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final MessageValidator messageValidator;
  private final MessageMapper messageMapper;

  @Override
  public MessageDto create(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests) {
    String content = messageCreateRequest.message();
    UUID channelId = messageCreateRequest.channelId();
    UUID authorId = messageCreateRequest.authorId();

    messageValidator.validateMessage(content);
    messageValidator.validateUserId(authorId);
    messageValidator.validateChannelId(channelId);

    List<UUID> attachmentIds = binaryContentCreateRequests.stream()
        .map(request -> {
          String fileName = request.fileName();
          String contentType = request.contentType();
          byte[] bytes = request.bytes();

          BinaryContent binaryContent = new BinaryContent(fileName, (long) bytes.length,
              contentType, bytes);
          BinaryContent createdbinaryContent = binaryContentRepository.save(binaryContent);
          return createdbinaryContent.getId();
        })
        .toList();

    Message message = new Message(content, channelId, authorId, attachmentIds);
    Message createdMessage = messageRepository.save(message);

    return messageMapper.messageEntityToDto(createdMessage);
  }

  @Override
  public MessageDto findById(UUID messageId) {
    return messageRepository.findById(messageId)
        .map(messageMapper::messageEntityToDto)
        .orElseThrow(() -> new ResourceNotFoundException("Message not found: " + messageId));
  }

  @Override
  public List<MessageDto> findAllByChannelId(UUID channelId) {
    return messageRepository.findAllByChannelId(channelId).stream()
        .map(messageMapper::messageEntityToDto)
        .toList();
  }

  @Override
  public MessageDto update(UUID messageId, MessageUpdateRequest request) {
    String newContent = request.newMessage();

    messageValidator.validateMessage(newContent);

    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new ResourceNotFoundException("Message not found: " + messageId));

    message.update(request.newMessage());
    Message updatedMessage = messageRepository.save(message);

    return messageMapper.messageEntityToDto(updatedMessage);
  }

  @Override
  public void delete(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(() -> new ResourceNotFoundException("Message not found: " + messageId));
    message.getAttachmentIds()
        .forEach(binaryContentRepository::deleteById);
    messageRepository.deleteById(messageId);
  }
}
