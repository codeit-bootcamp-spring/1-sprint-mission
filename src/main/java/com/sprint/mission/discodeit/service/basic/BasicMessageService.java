package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.global.error.execption.message.MessageNotFoundException;
import com.sprint.mission.discodeit.global.util.MultipartFileConverter;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.validator.ChannelValidator;
import com.sprint.mission.discodeit.validator.UserValidator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final BinaryContentRepository binaryContentRepository;

  private final UserValidator userValidator;
  private final ChannelValidator channelValidator;

  private final MultipartFileConverter multipartFileConverter;

  private final MessageMapper messageMapper;

  @Override
  public MessageDto createMessage(CreateMessageRequest createMessageRequest,
      List<MultipartFile> multipartFileList) {

    User sender = userValidator.validateUserExistsByUserId(createMessageRequest.authorId());
    Channel foundChannel = channelValidator.validateChannelExistsByChannelId(
        createMessageRequest.channelId());
    Message message = messageMapper.toEntity(sender, foundChannel, createMessageRequest.content());

    if (multipartFileList != null) {
      multipartFileList.forEach(
          multipartFile -> {
            // binaryContentService의 create와 똑같이 동작
            BinaryContent binaryContent = BinaryContent.of(multipartFile.getOriginalFilename(),
                multipartFile.getContentType(), multipartFileConverter.toByteArray(multipartFile));
            binaryContentRepository.saveBinaryContent(binaryContent);
            message.addBinaryContent(binaryContent);
          }
      );
    }

    List<UUID> attachmentIds = getAttachmentIds(message);

    return messageMapper.toMessageDto(messageRepository.saveMessage(message), attachmentIds);
  }

  // 요구 사항에 없기에 시간 남으면 수정
  @Override
  public MessageDto findMessageByIdOrThrow(UUID messageId) {
    // messageValidator 로 분리하는 것이 일관성 있지만 다른 클래스에서 참조 안하기에 일단 남겨둔다.
    Message message = findMessageById(messageId);

    List<UUID> attachmentIds = getAttachmentIds(message);

    return messageMapper.toMessageDto(message, attachmentIds);

  }

  @Override
  public List<MessageDto> findAllMessagesByChannelId(UUID channelId) {
    Channel channel = channelValidator.validateChannelExistsByChannelId(channelId);
    return messageRepository.findAllMessagesByChannel(channel).stream()
        .map(message -> messageMapper.toMessageDto(message, getAttachmentIds(message)))
        .toList();
  }

  @Override
  public MessageDto updateMessage(UUID messageId, UpdateMessageRequest updateMessageRequest) {
    Message foundMessage = findMessageById(messageId);

    foundMessage.updateContent(updateMessageRequest.newContent());

    List<UUID> attachmentIds = getAttachmentIds(foundMessage);

    return messageMapper.toMessageDto(messageRepository.saveMessage(foundMessage), attachmentIds);
  }

  @Override
  public void deleteMessage(UUID messageId) {
    Message foundMessage = findMessageById(messageId);

    foundMessage.getBinaryContentList()
        .forEach(
            binaryContent -> binaryContentRepository.removeBinaryContent(binaryContent.getId()));

    messageRepository.removeMessage(messageId);
  }

  private Message findMessageById(UUID messageId) {
    return Optional.ofNullable(messageRepository.findMessageById(messageId))
        .orElseThrow(() -> new MessageNotFoundException("id: " + messageId));
  }

  private List<UUID> getAttachmentIds(Message message) {
    return message.getBinaryContentList().stream()
        .map(binaryContent -> binaryContent.getId())
        .toList();
  }
}
