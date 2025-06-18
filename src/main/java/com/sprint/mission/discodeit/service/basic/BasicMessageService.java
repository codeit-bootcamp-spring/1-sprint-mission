package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.NotificationDto;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.reponse.PageResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.BinaryContentUploadStatus;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Notification;
import com.sprint.mission.discodeit.entity.NotificationType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.io.InputHandler;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.MessageService;
//
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import jakarta.transaction.Transactional;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
//
import java.util.*;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Service
@RequiredArgsConstructor
public class BasicMessageService implements MessageService {

  private final BinaryContentStorage binaryContentStorage;
  //
  private final MessageRepository messageRepository;
  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  //
  private final BinaryContentService binaryContentService;
  //
  private final MessageMapper messageMapper;
  private final BinaryContentMapper binaryContentMapper;
  //
  private final InputHandler inputHandler;
  private final PageResponseMapper<MessageDto> pageResponseMapper; // 제네릭 타입 명시 안해주면 Raw Type을 쓰고 있다고 경고를 준다.
  private final BinaryContentRepository binaryContentRepository;
  //
  private final TransactionTemplate transactionTemplate;
  private final ReadStatusRepository readStatusRepository;
  //
  private final ApplicationEventPublisher eventPublisher;


  @Transactional
  @Override
  public MessageDto createMessage(MessageCreateRequest messageCreateRequest,
      List<BinaryContentCreateRequest> binaryContentCreateRequests) {
    log.info("메세지 생성 시도: messageContent={}", messageCreateRequest.content());

    // 메세지 첨부 파일 생성
    List<BinaryContent> binaryContents = new ArrayList<>();
    if (binaryContentCreateRequests != null) {
      log.info("메세지 첨부 파일 생성");

      for (BinaryContentCreateRequest req : binaryContentCreateRequests) {

        BinaryContent binaryContent = BinaryContent
            .builder()
            .fileName(req.fileName())
            .size(req.size())
            .contentType(req.contentType())
            .uploadStatus(BinaryContentUploadStatus.WAITING)
            .build();

        binaryContents.add(binaryContent);

        BinaryContent content = binaryContentRepository.save(binaryContent);
        log.info("메세지 첨부 파일 DB에 저장, contentId={}", content.getId());

        TransactionSynchronizationManager.registerSynchronization(
            new TransactionSynchronization() {
              @Override
              public void afterCommit() {

                CompletableFuture<UUID> future = binaryContentStorage.put(content.getId(),
                    req.bytes());

                future.thenAccept(
                        fileId -> {

                          transactionTemplate.execute(status -> {
                            log.info("파일 업로드 성공, SUCCESS 로 상태 변경: contentId={}", content.getId());
                            binaryContentRepository.updateStatusById(content.getId(),
                                BinaryContentUploadStatus.SUCCESS);
                            return null;
                          });

                        })
                    .exceptionally(ex -> {

                      transactionTemplate.execute(status -> {
                        binaryContentRepository.updateStatusById(content.getId(),
                            BinaryContentUploadStatus.FAILED);
                        log.error("파일 업로드 실패, FAILED 로 상태 변경: contentId={}, message={}",
                            content.getId(),
                            ex.getMessage(), ex);
                        return null;
                      });
                      return null;
                    });
              }
            }
        );

      }
    }

    // 메세지 생성

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
        .attachments(
            binaryContents
        )
        .author(user)
        .channel(channel)
        .build();

    messageRepository.save(message);

    // 알림 생성 (분리될 거 생각하고 우선 구현)
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

    log.info("메세지 생성 시도 성공: messageContent={}, createdAt={}",
        message.getContent(),
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
