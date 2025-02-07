package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binary_content.BinaryContentDto;
import com.sprint.mission.discodeit.dto.MessageUpdateDto;
import com.sprint.mission.discodeit.dto.message.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.validator.EntityValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@ConditionalOnProperty(name = "app.service.type", havingValue = "basic")
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final EntityValidator validator;


  @Override
  public MessageResponseDto createMessage(CreateMessageDto messageDto) {

    // 사용자 검증
    validator.findOrThrow(User.class, messageDto.userId(), new UserNotFoundException());

    // 채널 검증
    validator.findOrThrow(Channel.class, messageDto.channelId(), new ChannelNotFoundException());

    Message message = new Message.MessageBuilder(messageDto.userId(), messageDto.channelId(), messageDto.content()).build();

    List<BinaryContentDto> binaryContentDtos = messageDto.binaryContent();

    //TODO : 한번에 save 하도록
    for (BinaryContentDto binary : binaryContentDtos) {
      BinaryContent content = new BinaryContent.BinaryContentBuilder(
          messageDto.userId(),
          binary.fileName(),
          binary.fileType(),
          binary.fileSize(),
          binary.data()
      ).messageId(message.getUUID())
          .channelId(messageDto.channelId()).build();

      binaryContentRepository.save(content);
    }

    messageRepository.create(message);

    return MessageResponseDto.fromBinaryContentDto(message, binaryContentDtos);
  }

  @Override
  public Optional<Message> getMessageById(String messageId, Channel channel) {

    return messageRepository.findById(messageId);
  }


  /**
   * {@link MessageResponseDto} = 메시지 + {@link BinaryContentDto} 형태
   * 하나의 메시지는 여러 첨부파일을 가질 수 있으므로, messageIdToBinaryContentMap 에 messageId : List<BinaryContent>
   * 이후 message와 해당 messageid 에 해당하는 BinaryContents들로 Dto 생성
   * 하나의 MessageResponseDto = 하나의 메시지에 대한 정보
   *
   * @param channelId
   * @return
   */
  @Override
  public List<MessageResponseDto> getMessagesByChannel(String channelId) {

    List<Message> channelMessages = messageRepository.findByChannel(channelId);

    // 메시지 id : binaryContents
    Map<String, List<BinaryContent>> messageIdToBinaryContentMap =
        binaryContentRepository.findByChannel(channelId).stream()
            .collect(Collectors.groupingBy(
                BinaryContent::getMessageId,
                Collectors.toList()
            ));


    return channelMessages.stream()
        .map(
            message -> MessageResponseDto
                .fromBinaryContent(message, messageIdToBinaryContentMap.getOrDefault(message.getUUID(), Collections.emptyList())))
        .toList();
  }

  @Override
  public MessageResponseDto updateMessage(MessageUpdateDto messageDto) {


    Message originalMessage = messageRepository.findById(messageDto.messageId()).orElseThrow(
        MessageNotFoundException::new
    );

    List<BinaryContent> updatedContents = findAndUpdateBinaryContent(messageDto.messageId(), messageDto.userId(), messageDto.binaryContent());

    synchronized (originalMessage) {
      originalMessage.setContent(messageDto.content());
    }

    messageRepository.update(originalMessage);
    return MessageResponseDto.fromBinaryContent(originalMessage, updatedContents);
  }

  // TODO : messageId 가 아닌 Message 자체를 넘기도록 (지금 중복임)
  private List<BinaryContent> findAndUpdateBinaryContent(String messageId, String userId, List<BinaryContentDto> binaryDtos) {

    List<BinaryContent> originalBinaryContent = binaryContentRepository.findByMessageId(messageId);

    Message message = messageRepository.findById(messageId).orElseThrow(
        () -> new MessageNotFoundException()
    );

    for (BinaryContentDto dto : binaryDtos) {
      BinaryContent existingContent = originalBinaryContent.stream()
          .filter(content -> content.getFileName().equals(dto.fileName()))
          .findFirst()
          .orElse(null);

      if (existingContent == null) {
        BinaryContent newBinaryContent = new BinaryContent.BinaryContentBuilder(userId, dto.fileName(), dto.fileType(), dto.fileSize(), dto.data())
            .messageId(messageId)
            .channelId(message.getChannelUUID()).build();
        originalBinaryContent.add(newBinaryContent);
      }
    }

    return binaryContentRepository.saveMultipleBinaryContent(originalBinaryContent);
  }


  @Override
  public void deleteMessage(String messageId) {
    binaryContentRepository.deleteByMessageId(messageId);
    messageRepository.delete(messageId);
  }
}
