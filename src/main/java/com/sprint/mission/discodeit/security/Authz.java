package com.sprint.mission.discodeit.security;

import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Authz {

  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;

  public boolean isMessageOwner(UUID messageId, UUID userId) {
    return messageRepository.isAuthor(messageId, userId);
  }

  public boolean isReadStatusOwner(UUID readStatusId, UUID userID) {
    return readStatusRepository.isOwner(readStatusId, userID);
  }

}
