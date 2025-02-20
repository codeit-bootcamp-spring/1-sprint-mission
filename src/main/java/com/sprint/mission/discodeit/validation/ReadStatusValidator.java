package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.exception.duplication.DuplicateResourceException;
import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReadStatusValidator {

  private final ChannelRepository channelRepository;
  private final UserRepository userRepository;
  private final ReadStatusRepository readStatusRepository;

  public void validateChannel(UUID channelId) {
    if (channelRepository.findById(channelId).isEmpty()) {
      throw new ResourceNotFoundException("Channel not found: " + channelId);
    }
  }

  public void validateUser(UUID userId) {
    if (userRepository.findById(userId).isEmpty()) {
      throw new ResourceNotFoundException("User not found: " + userId);
    }
  }

  public void validateDuplicateReadStatus(UUID channelId, UUID userId) {
    if (readStatusRepository.findAllByUserId(userId).stream()
        .anyMatch(readStatus -> readStatus.getChannelId().equals(channelId))) {
      throw new DuplicateResourceException(
          "ReadStatus already exists. " + "User id: " + userId + ". Channel id: " + channelId);
    }
  }

  public void validateReadStatus(UUID readStatusId) {
    if (readStatusRepository.findById(readStatusId).isEmpty()) {
      throw new ResourceNotFoundException("Read status not found: " + readStatusId);
    }
  }
}
