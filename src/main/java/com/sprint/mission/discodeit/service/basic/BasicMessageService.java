package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.async.BinaryContentUploadExecutor;
import com.sprint.mission.discodeit.dto.PageResponse;
import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateDTO;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.message.MessageUpdateDTO;
import com.sprint.mission.discodeit.entity.*;

import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.jpa.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.jpa.ChannelRepository;
import com.sprint.mission.discodeit.repository.jpa.MessageRepository;
import com.sprint.mission.discodeit.repository.jpa.UserRepository;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

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
  private final BinaryContentUploadExecutor uploadExecutor;


  @Override
  @Transactional
  public MessageDto create(MessageCreateDTO dto,
      List<BinaryContentCreateRequest> binaryContentCreateRequests) {
    User findUser = userRepository.findById(dto.getAuthorId())
        .orElseThrow(() -> new UserNotFoundException(dto.getAuthorId()));

    Channel findChannel = channelRepository.findById(dto.getChannelId())
        .orElseThrow(() -> new ChannelNotFoundException(dto.getChannelId()));

    Message message = new Message(dto.getContent(), findUser, findChannel);

    binaryContentCreateRequests.stream()
        .map(attachmentRequest -> {
          BinaryContent binaryContent = new BinaryContent(
              attachmentRequest.getFileName(),
              attachmentRequest.getContentType(),
              (long) attachmentRequest.getBytes().length
          );

//          BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);
//          binaryContentStorage.put(savedBinaryContent.getId(), attachmentRequest.getBytes());
//          return savedBinaryContent;

          binaryContent.updateUploadStatus(BinaryContentUploadStatus.WAITING);
          BinaryContent savedBinaryContent = binaryContentRepository.save(binaryContent);

          // 트랜잭션 커밋 이후 비동기 업로드 실행
          TransactionSynchronizationManager.registerSynchronization(
                  new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                      String requestId = MDC.get("requestId");
                      uploadExecutor.uploadAsync(savedBinaryContent.getId(), attachmentRequest.getBytes(), requestId);
                    }
                  }
          );
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
        .orElseThrow(() -> new MessageNotFoundException(id));
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
        .orElseThrow(() -> new MessageNotFoundException(id));
    findMessage.setMessage(dto.getNewContent());

    log.info("메시지 수정 완료 id: {}", findMessage.getId());
    return messageMapper.toDto(findMessage);
  }

  @Override
  public void delete(UUID id) {
    if (!messageRepository.existsById(id)) {
      throw new MessageNotFoundException(id);
    }
    //binaryContent ddl - on delete cascade
    messageRepository.deleteById(id);
    log.info("메시지 삭제 완료 id: {}", id);
  }


}
