package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.NotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.validator.MessageValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final BinaryContentService binaryContentService;
  private final MessageValidator messageValidator;
  private final BinaryContentRepository binaryContentRepository;

  @Override
  public Message create(MessageCreateDTO dto,
      List<BinaryContentCreateRequest> binaryContentCreateRequests) {
    messageValidator.validateMessage(dto.getContent(), dto.getAuthorId(), dto.getChannelId());

    List<UUID> attachmentIds = binaryContentCreateRequests.stream()
        .map(attachmentRequest -> {
          String fileName = attachmentRequest.getFileName();
          String contentType = attachmentRequest.getContentType();
          byte[] bytes = attachmentRequest.getBytes();

          return binaryContentRepository.save(
              new BinaryContent(bytes, fileName, contentType, (long) bytes.length)).getId();
        }).toList();

    Message message = new Message(dto.getContent(), dto.getAuthorId(), dto.getChannelId(),
        attachmentIds);

    return messageRepository.save(message);
  }

  @Override
  public Message find(UUID id) {
    Message findMessage = messageRepository.findById(id);
    return Optional.ofNullable(findMessage)
        .orElseThrow(() -> new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND));
  }

  @Override
  public List<Message> findAll() {
    return messageRepository.findAll();
  }

  @Override
  public List<Message> findAllByChannelId(UUID channelId) {
    return messageRepository.findAllByChannelId(channelId).stream().toList();
  }

  @Override
  public Message update(UUID id, MessageUpdateDTO dto) {
    Message findMessage = messageRepository.findById(id);

    findMessage.setMessage(dto.getNewContent());
    messageRepository.update(findMessage);
    return findMessage;
  }

  @Override
  public void delete(UUID id) {
    Message findMessage = messageRepository.findById(id);

    for (UUID binaryContentId : findMessage.getAttachmentIds()) {
      binaryContentService.delete(binaryContentId);
    }
    messageRepository.delete(findMessage.getId());
  }
}
