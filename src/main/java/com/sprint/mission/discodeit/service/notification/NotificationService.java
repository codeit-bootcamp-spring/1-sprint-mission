package com.sprint.mission.discodeit.service.notification;

import com.sprint.mission.discodeit.dto.response.NotificationDto;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.entity.notification.Notification;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

  private final NotificationRepository notificationRepository;

  /**
   * @methodName : findAll
   * @date : 2025-06-04 오후 4:30
   * @author : wongil
   * @Description: 모든 알림 가져오기(자신만 알림 볼 수 있음)
   **/
//  @PreAuthorize("@authorizationChecker.isNotiUser(#userDto.id())")
  @Cacheable(cacheNames = "noti", key = "#userDto.id", sync = true)
  public List<NotificationDto> findAll(UserDto userDto) {

    List<Notification> notifications = notificationRepository.findAllByReceiver_IdOrderByCreatedAtDesc(
        userDto.id());
    if (notifications.isEmpty()) {
      return new ArrayList<>();
    }

    return notifications.stream()
        .map(noti -> new NotificationDto(noti.getId(), noti.getCreatedAt(),
            noti.getReceiver().getId(),
            noti.getTitle(), noti.getContent(), noti.getType(), Optional.of(noti.getTargetId())))
        .toList();
  }

  /**
   * @methodName : check
   * @date : 2025-06-04 오후 4:36
   * @author : wongil
   * @Description: 알림 삭제(확인)
   **/
  @CacheEvict(cacheNames = "noti", allEntries = true, beforeInvocation = true)
  @PreAuthorize("@authorizationChecker.isNotiUser(#userDto.id())")
  public void check(UserDto userDto, UUID notificationId) {

    if (notificationRepository.existsByReceiver_Id(userDto.id())) {
      notificationRepository.deleteById(notificationId);
    } else {
      throw new UserNotFoundException(Instant.now(), ErrorCode.USER_NOT_FOUND, Map.of(
          ErrorCode.USER_NOT_FOUND.getCode(),
          ErrorCode.USER_NOT_FOUND.getMessage()
      ));
    }


  }
}
