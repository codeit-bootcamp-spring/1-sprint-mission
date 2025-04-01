package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.PageResponse;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDTO;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.NotFoundException;

import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.jpa.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.jpa.ChannelRepository;
import com.sprint.mission.discodeit.repository.jpa.MessageRepository;
import com.sprint.mission.discodeit.repository.jpa.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.validator.MessageValidator;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {


  private final MessageRepository messageRepository;

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;
  private final MessageMapper messageMapper;
  private final BinaryContentRepository binaryContentRepository;
  private final BinaryContentStorage binaryContentStorage;

  @Override
  @Transactional
  public MessageDto create(MessageCreateDTO dto,
      List<BinaryContentCreateRequest> binaryContentCreateRequests) {
    User findUser = userRepository.findById(dto.getAuthorId())
        .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));

    Channel findChannel = channelRepository.findById(dto.getChannelId())
        .orElseThrow(() -> new NotFoundException(ErrorCode.CHANNEL_NOT_FOUND));

    Message message = new Message(dto.getContent(), findUser, findChannel);

    binaryContentCreateRequests.stream()
        .map(attachmentRequest -> {
          BinaryContent binaryContent = new BinaryContent(
              attachmentRequest.getFileName(),
              attachmentRequest.getContentType(),
              (long) attachmentRequest.getBytes().length
          );

          BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);
          binaryContentStorage.put(savedBinaryContent.getId(), attachmentRequest.getBytes());
          return savedBinaryContent;
        })
        .forEach(message::addAttachments);

    messageRepository.save(message);

    log.info("메시지 생성 완료 id: {}", message.getId());
    return messageMapper.toDto(message);
  }


  //no use
  @Override
  @Transactional(readOnly = true)
  public MessageDto find(UUID id) {
    Message message = messageRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND));
    return messageMapper.toDto(message);
  }


  @Override
  @Transactional(readOnly = true)
  public PageResponse<MessageDto> findAllByChannelId(UUID channelId, Instant cursor,
      Pageable pageable) {
    Page<Message> messagePage;
    boolean hasNext;
    Long totalElements = null;

    // 커서가 없으면 첫 페이지 요청, 커서가 있으면 해당 커서 이후 데이터 요청
    if (cursor == null) {
      messagePage = messageRepository.findAllByChannel_Id(channelId, pageable);
      hasNext = messagePage.hasNext();
      totalElements = messagePage.getTotalElements();
    } else {
      messagePage = messageRepository.findAllByChannel_IdAndCreatedAtBefore(channelId, cursor,
          pageable);
      hasNext = messagePage.hasNext();
      totalElements = messagePage.getTotalElements();
    }

    // 메시지 DTO로 변환
    List<MessageDto> messageDtos = messagePage.getContent().stream()
        .map(messageMapper::toDto)
        .toList();

    // 다음 페이지의 커서값 설정 (최신 메시지의 createdAt 값)
    Instant nextCursor = null;
    if (hasNext && !messagePage.getContent().isEmpty()) {
      nextCursor = messagePage.getContent().get(messagePage.getContent().size() - 1).getCreatedAt();
    }

    return new PageResponse<>(messageDtos, nextCursor, pageable.getPageSize(), hasNext,
        totalElements);
  }

  @Override
  @Transactional
  public MessageDto update(UUID id, MessageUpdateDTO dto) {
    Message findMessage = messageRepository.findById(id)
        .orElseThrow(() -> new NotFoundException(ErrorCode.MESSAGE_NOT_FOUND));
    findMessage.setMessage(dto.getNewContent());

    log.info("메시지 수정 완료 id: {}", findMessage.getId());
    return messageMapper.toDto(findMessage);
  }

  @Override
  public void delete(UUID id) {
    //binaryContent ddl - on delete cascade
    messageRepository.deleteById(id);
    log.info("메시지 삭제 완료 id: {}", id);
  }


}
