package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.dto.AsyncTaskFailure;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.NotificationService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

  private final NotificationService notificationService;
  private final ReadStatusRepository readStatusRepository;

  @Async("eventExecutor")
  @TransactionalEventListener
  @Retryable(
      retryFor = Exception.class,
      backoff = @Backoff(delay = 2000)
  )
  public void handle(NewMessageEvent event) {
    log.info("새 메세지 생성 이벤트 발생");
    MessageDto messageDto = event.messageDto();
    ChannelDto channelDto = event.channelDto();

    UserDto authorDto = messageDto.author();
    String title = channelDto.type().equals(Channel.Type.PUBLIC)
        ? String.format("%s (# %s)", authorDto.username(), channelDto.name())
        : authorDto.username();
    String content = messageDto.content();

    List<UUID> userIds = readStatusRepository
        .findByChannelIdAndNotificationEnabledTure(messageDto.channelId())
        .stream()
        .map(readStatus -> readStatus.getUser().getId())
        .filter(userId -> !userId.equals(messageDto.author().id()))
        .toList();

    notificationService.create(userIds, title, content, event.type(), channelDto.id());
  }

  @Async("eventExecutor")
  @TransactionalEventListener
  @Retryable(
      retryFor = Exception.class,
      backoff = @Backoff(delay = 2000)
  )
  public void handle(RoleChangedEvent event) {
    log.info("역할 변경 이벤트 발생");
    String title = String.format("권한 변경: %s -> %s", event.oldRole(), event.newRole());
    String content = String.format("관리자에 의해 권한이 '%s'(으)로 변경되었습니다.", event.newRole());
    notificationService.create(
        event.userId(),
        title,
        content,
        event.type(),
        event.userId()
    );
  }

  @Async("eventExecutor")
  @EventListener
  @Retryable(
      retryFor = Exception.class,
      backoff = @Backoff(delay = 2000)
  )
  public void handle(AsyncFailedEvent event) {
    log.info("비동기 파일 업로드 실패 이벤트 발생");
    AsyncTaskFailure asyncTaskFailure = event.asyncTaskFailure();

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication == null || !authentication.isAuthenticated()
        || !(authentication.getPrincipal() instanceof DiscodeitUserDetails principal)) {
      log.error("비동기 파일 업로드 실패: 인증되지 않은 사용자");
      return;
    }

    UUID id = principal.getUser().id();
    String title = String.format("비동기 작업 실패: %s", asyncTaskFailure.taskName());
    String content = String.format("요청 ID: %s\n실패 사유: %s", asyncTaskFailure.requestId(),
        asyncTaskFailure.failureReason());

    notificationService.create(id, title, content, event.type(), null);
  }
}
