package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.MessageDto;
import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final BinaryContentRepository binaryContentRepository;
  private final MessageMapper messageMapper;
  private final PageResponseMapper pageResponseMapper;


  @Transactional
  @Override
  public MessageDto create(MessageCreateRequest request,
      List<BinaryContentCreateRequest> attachmentRequests) {
    log.debug("create() called with - channelId: {}, authorId: {}, content: {}",
        request.channelId(), request.authorId(), request.content());

    Channel channel = channelRepository.findById(request.channelId())
        .orElseThrow(() -> {
          log.error("메세지 생성 실패 - 존재하지 않는 채널 ID: {}", request.channelId());
          return new ChannelNotFoundException(request.channelId());
        });

    User author = userRepository.findById(request.authorId())
        .orElseThrow(() -> {
          log.error("메세지 생성 실패 - 존재 하지 않는 작성자 ID: {}", request.authorId());
          return new UserNotFoundException(request.authorId());
        });

    List<BinaryContent> attachmentIds = Optional.ofNullable(attachmentRequests)
        .orElse(List.of())
        .stream()
        .map(attachmentRequest -> {
          log.debug("첨부 파일 저장 - filename : {}", attachmentRequest.fileName());
          BinaryContent binaryContent = new BinaryContent(
              UUID.randomUUID(),
              attachmentRequest.fileName(),
              (long) attachmentRequest.bytes().length,
              attachmentRequest.contentType()
          );
          return binaryContentRepository.save(binaryContent);
        })
        .toList();
    Message message = new Message(request.content(), channel, author, attachmentIds);
    message = messageRepository.save(message);
    return messageMapper.toDto(message);

  }

  @Override
  public MessageDto find(UUID messageId) {
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> new MessageNotFoundException(messageId));
    return messageMapper.toDto(message);
  }

  @Override
  public PageResponse<MessageDto> findAllByChannelId(UUID channelId, int page) {
    Pageable pageable = PageRequest.of(page, 50);
    Slice<Message> slice = messageRepository
        .findAllByChannelIdOrderByCreatedAtDesc(channelId, pageable);
    List<MessageDto> messageDtos = slice.getContent().stream()
        .map(messageMapper::toDto)
        .toList();

    return new PageResponse<>(messageDtos, slice.getNumber(), slice.getSize(), slice.hasNext(),
        null);
  }


  @Transactional
  @Override
  public MessageDto update(UUID messageId, MessageUpdateRequest request) {
    log.debug("update() called with - messageId: {}, newMessage: {}",
        messageId, request.newContent());
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> {
              log.error("메세지 수정 실패 - 존재 하지 않는 ID: {}", messageId);
              return new MessageNotFoundException(messageId);
            });
    message.update(request.newContent());
    return messageMapper.toDto(message);
  }

  @Transactional
  @Override
  public void delete(UUID messageId) {
    log.debug("delete() called with - messageId: {}", messageId);
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> {
              log.error("메세지 삭제 실패 - 존재하지 않는 ID: {}", messageId);
              return new MessageNotFoundException(messageId);
            });

    Optional.ofNullable(message.getAttachments())
        .orElse(List.of())
        .stream()
        .map(BinaryContent::getId)
        .forEach(binaryContentRepository::deleteById);

    messageRepository.deleteById(messageId);
  }
}
