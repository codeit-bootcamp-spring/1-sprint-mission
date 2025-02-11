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
import com.sprint.mission.discodeit.exception.InvalidOperationException;
import com.sprint.mission.discodeit.exception.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.UserNotFoundException;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.validator.EntityValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.sprint.mission.discodeit.constant.ErrorConstant.DEFAULT_ERROR_MESSAGE;

@Slf4j
@Service
@ConditionalOnProperty(name = "app.service.type", havingValue = "basic")
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final EntityValidator validator;

  private final BinaryContentService binaryContentService;


  /**
   * 메시지를 생성하고 첨부 파일을 저장한다
   * <p>
   * 1) 사용자 / 채널 검증
   * 2) Message 생성 + 저장
   * 3) 첨부파일 저장
   * 4) MessageResponseDto 생성 및 반환
   * </p>
   *
   * @param messageDto
   * @return {@link MessageResponseDto} 기본적인 메시지 정보 & 같이 첨부된 파일 데이터
   */
  @Override
  public MessageResponseDto createMessage(CreateMessageDto messageDto) {

    validateCreateMessageRequest(messageDto);

    Message message = new Message.MessageBuilder(messageDto.userId(), messageDto.channelId(), messageDto.content()).build();

    messageRepository.create(message);

    List<BinaryContentDto> binaryContentDtos = binaryContentService.saveBinaryContentsForMessage(messageDto, message.getUUID());

    return MessageResponseDto.fromBinaryContentDto(message, binaryContentDtos);
  }

  /**
   * 사용자, 채널의 존재 여부 검증
   * 사용자가 해당 채널에 속해 있는지 검증
   *
   * @param messageDto 메시지 생성 정보
   * @return 존재하는 검증 완료된 Channel 엔티티
   */
  private Channel validateCreateMessageRequest(CreateMessageDto messageDto) {
    validator.findOrThrow(User.class, messageDto.userId(), new UserNotFoundException());
    Channel channel = validator.findOrThrow(Channel.class, messageDto.channelId(), new ChannelNotFoundException());
    checkIfUserBelongsToPrivateChannel(channel, messageDto);
    return channel;
  }

  /**
   * Private 채널에 속하지 않은 User 가 해당 채널에 메시지를 작성하려는 경우 예외
   *
   * @param channel    대상 채널
   * @param messageDto 메시지 생성 정보
   */
  private void checkIfUserBelongsToPrivateChannel(Channel channel, CreateMessageDto messageDto) {
    if (channel.getIsPrivate()) {
      channel.getParticipatingUsers().stream().filter(id -> id.equals(messageDto.userId())).findAny().orElseThrow(
          () -> new InvalidOperationException(DEFAULT_ERROR_MESSAGE)
      );
    }
  }

  @Override
  public MessageResponseDto getMessageById(String messageId) {
    List<BinaryContent> contents = binaryContentService.findByMessageId(messageId);
    Message message = messageRepository.findById(messageId).orElseThrow(MessageNotFoundException::new);
    return MessageResponseDto.fromBinaryContent(message, contents);
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
    Map<String, List<BinaryContent>> messageIdToBinaryContentMap = binaryContentService.getBinaryContentsFilteredByChannelAndGroupedByMessage(channelId);

    return buildMessageResponseDtos(channelMessages, messageIdToBinaryContentMap);
  }


  /**
   * 메시지 목록과 (messageId: BinaryContent 목록) 맵을 이용해
   * 각 메시지 마다 {@link MessageResponseDto} 를 생성
   *
   * @param messages                    메시지 리스트
   * @param messageIdToBinaryContentMap (messageId : BinaryContent 목록)
   * @return MessageResponseDto 목록
   */
  private List<MessageResponseDto> buildMessageResponseDtos(
      List<Message> messages,
      Map<String, List<BinaryContent>> messageIdToBinaryContentMap
  ) {

    return messages.stream()
        .map(
            message -> MessageResponseDto
                .fromBinaryContent(message, messageIdToBinaryContentMap.getOrDefault(message.getUUID(), Collections.emptyList())))
        .toList();
  }

  /**
   * {@link MessageUpdateDto} 를 받아 메시지를 업데이트 한다
   * <p>
   *   1) 메시지, 사용자 검증 후 기존 메시지를 찾는다
   *   2) 메시지의 BinaryContent를 업데이트 한다
   *   3) 메시지의 내용을 업데이트 한다
   *   4) 업데이트 된 메시지와 BinaryContent 로 {@link MessageResponseDto}
   * </p>
   * @param messageDto 메시지 업데이트 정보
   * @return 업데이트 된 메시지 & BinaryContent
   */
  @Override
  public MessageResponseDto updateMessage(MessageUpdateDto messageDto) {

    Message originalMessage = validateMessageUpdateRequest(messageDto);

    List<BinaryContent> updatedContents = binaryContentService.updateBinaryContentForMessage(
        originalMessage,
        messageDto.userId(),
        messageDto.binaryContent()
    );

    updateMessageContent(originalMessage, messageDto.content());

    return MessageResponseDto.fromBinaryContent(originalMessage, updatedContents);
  }

  /**
   * 메시지 요청의 유효성을 검증한다
   * <p>
   *   1) 메시지 존재 여부
   *   2) 사용자 존재 여부
   *   3) 사용자가 메시지 작성자인지 확인
   * </p>
   * @param messageDto 메시지 업데이트 정보
   * @return 검증된 Message
   */
  private Message validateMessageUpdateRequest(MessageUpdateDto messageDto) {
    Message message = validator.findOrThrow(Message.class, messageDto.messageId(), new MessageNotFoundException());
    validator.findOrThrow(User.class, messageDto.userId(), new UserNotFoundException());

    if (!message.getUserUUID().equals(messageDto.userId())) {
      log.warn("해당 사용자의 메시가 아닙니다. user={} message{}", messageDto.userId(), messageDto.messageId());
      throw new InvalidOperationException(DEFAULT_ERROR_MESSAGE);
    }

    return message;
  }

  /**
   * 메시지의 내용을 업데이트 한다
   * @param message 검증된 Message
   * @param newContent 수정 될 내용
   */
  private void updateMessageContent(Message message, String newContent) {

    synchronized (message) {
      message.setContent(newContent);
    }

    messageRepository.update(message);
  }

  @Override
  public void deleteMessage(String messageId) {
    binaryContentService.deleteByMessageId(messageId);
    messageRepository.delete(messageId);
  }
}
