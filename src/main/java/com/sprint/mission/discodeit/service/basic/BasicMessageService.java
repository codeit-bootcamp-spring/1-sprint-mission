package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.MessageImageDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.NotificationDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.reponse.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
//
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
//
import java.util.*;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  //
  private final BinaryContentService binaryContentService;
  //
  private final MessageMapper messageMapper;
  //
  private final PageResponseMapper<MessageDto> pageResponseMapper; // 제네릭 타입 명시 안해주면 Raw Type을 쓰고 있다고 경고를 준다.
  //
  private final ReadStatusRepository readStatusRepository;
  //
  private final ApplicationEventPublisher eventPublisher;


  @Transactional
  @Override
  public MessageDto createMessage(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests) {
    log.info("메세지 생성 시도: messageContent={}", messageCreateRequest.content());

    // 1. 메세지 첨부 파일 생성
    List<BinaryContent> binaryContents = new ArrayList<>();
    if (binaryContentCreateRequests != null) {
      log.info("메세지 첨부 파일 생성. 첨부된 파일 개수: {}", binaryContentCreateRequests.size());
      List<Object> fileInfo = new ArrayList<>();
      List<List<Object>> fileInfoList = new ArrayList<>();
      for (BinaryContentCreateRequest req : binaryContentCreateRequests) {
        BinaryContent content = binaryContentService.createBinaryContent(req);
        binaryContents.add(content);

        log.info("메세지 첨부 파일 DB에 저장, contentId={}", content.getId());

        // SSE 이벤트 전송용 데이터
        fileInfo.add(content.getId());
        fileInfo.add(req.bytes());
        fileInfo.add(messageCreateRequest.authorId());

        fileInfoList.add(fileInfo);
      }

      MessageImageDto uploadDto = MessageImageDto.builder()
          .fileInfoList(fileInfoList)
          .build();

      // 5. 커밋 후 메세지 이미지 업로드 및 알림 관련 이벤트 발행 -> 즉시 처리x 큐에 등록
      eventPublisher.publishEvent(uploadDto);
    }

    // 2. 메세지 객체 생성
    User user = userRepository.findById(messageCreateRequest.authorId())
        .orElseThrow(() -> {
          log.error("메세지 생성 단계에서 유저를 찾지 못함: userId={}", messageCreateRequest.authorId());
          return new UserNotFoundException(
              Map.of("UserId", messageCreateRequest.authorId()));
        });
    Channel channel = channelRepository.findById(messageCreateRequest.channelId())
        .orElseThrow(() -> {
          log.error("메세지 생성 단계에서 채널을 찾지 못함: channelId={}",
              messageCreateRequest.channelId());
          return new ChannelNotFoundException(
              Map.of("channelId", messageCreateRequest.channelId()));
        });
    Message message = Message.builder()
        .content(messageCreateRequest.content())
        .attachments(binaryContents)
        .author(user)
        .channel(channel)
        .build();
    messageRepository.save(message);

    // 3. 메세지 생성 알림 생성 (분리될 거 생각하고 우선 구현)
    List<ReadStatus> readStatuses = readStatusRepository.findByChannelId(channel.getId());
    log.info("알림을 생성합니다. channelId={}", channel.getId());

    for (ReadStatus r : readStatuses) {
      if (r != null && r.isNotificationEnabled()) {
        UUID receiverId = r.getUser().getId();
        eventPublisher.publishEvent(
            NotificationDto.builder()
                .title(channel.getName())
                .content(messageCreateRequest.content())
                .type(NotificationType.NEW_MESSAGE)
                .receiverId(receiverId)
                .targetId(channel.getId())
                .build()
        );
      }
    }

    // 4. MessageDto 반환
    log.info("메세지 생성 시도 성공: messageContent={}, message.getAttachments()={} createdAt={}",
        message.getContent(),
        message.getAttachments(),
        message.getCreatedAt());
    log.info("메세지 생성 시도 성공: messageID={}",
        message.getId());
    return messageMapper.toDto(message);
  }

  // 페이징 처리가 필요
  @Override
  public PageResponse<MessageDto> findAllByChannelId(
      @RequestParam UUID channelId, Pageable pageable) {
    // 왜 이렇게 변환하는거지 (이해가 필요...)

    channelRepository.findById(channelId).orElseThrow(() -> {
      return new ChannelNotFoundException(Map.of("channelId", channelId));
    });

    // 페이징된 데이터 조회
    Page<Message> messagePage = messageRepository.findByChannelId(channelId, pageable);

    // Message -> MessageDto, Page<T> 인터페이스가 기본적으로 map(Function<T, R> converter) 메서드 제공
    Page<MessageDto> messageDtoPage = messagePage.map(messageMapper::toDto);

    // Page -> PageResponse<MessageDto> 필요한 정보만 담기
    return pageResponseMapper.fromPage(messageDtoPage);
  }

  @Override
  public MessageDto getMessageById(UUID id) {
    Message message =
        messageRepository.findById(id)
            .orElseThrow(() -> new MessageNotFoundException(Map.of("messageId", id)));
    return messageMapper.toDto(message);
  }

  @PreAuthorize("@messageRepository.findById(#messageId).orElse(null)?.author.id == authentication.principal.id")
  @Transactional
  @Override
  public MessageDto updateMessageText(UUID messageId, MessageUpdateRequest messageUpdateRequest) {

    // 메세지 존재 유무 확인
    Message message = messageRepository.findById(messageId)
        .orElseThrow(
            () -> {
              log.error("메세지 수정 단계에서 메세지를 찾지 못함: messageId={}", messageId);
              return new MessageNotFoundException(Map.of("messageId", messageId));
            });

    log.info("메세지 수정 시도: originalMessage={}", message.getContent());

    message.updateMessageText(messageUpdateRequest.newMessage());
    message.refreshUpdateAt();

    log.info("메세지 수정 시도 성공: newMassage={}, updatedAt={}",
        message.getContent(),
        message.getUpdatedAt());
    // JPA 의 더티 채킹으로 save 하지 않아도 DB에 자동 업데이트
    return messageMapper.toDto(message);
  }

  @PreAuthorize("hasRole('ADMIN') or @messageRepository.findById(#id).orElse(null)?.author.id == authentication.principal.id")
  @Transactional
  @Override
  public void deleteMessageById(UUID id) {
    log.info("메세지 삭제 시도");

    // 메세지 찾기
    if (messageRepository.findById(id).isEmpty()) {
      log.error("메세지 삭제 단계에서 메세지를 찾지 못함: messageId={}", id);
      throw new MessageNotFoundException(Map.of("messageId", id));
    }

    log.info("메세지 첨부 파일 삭제");
    // 메세지 첨부 파일 삭제
    messageRepository.findById(id).stream()
        .map(Message::getAttachments)
        .flatMap(List::stream) // List에서 하나씩
        .map(BaseEntity::getId)
        .forEach(binaryContentService::deleteBinaryContentById);

    // 메세지 삭제
    messageRepository.deleteById(id);
    log.info("메세지 삭제 시도 성공");
  }
}
