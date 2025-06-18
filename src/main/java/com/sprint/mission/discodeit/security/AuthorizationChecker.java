package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.NotificationRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorizationChecker {

  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;
  private final NotificationRepository notificationRepository;

  public boolean isMessageAuthor(UUID messageId, UUID userId) {
    return messageRepository.existsByidAndAuthor_Id(messageId, userId);
  }

  public boolean isReadStatusUser(UUID readStatusId, UUID userId) {
    return readStatusRepository.existsByIdAndUser_id(readStatusId, userId);
  }

  public boolean isNotiUser(UUID userId) {
    return notificationRepository.existsByReceiver_Id(userId);
  }

}
