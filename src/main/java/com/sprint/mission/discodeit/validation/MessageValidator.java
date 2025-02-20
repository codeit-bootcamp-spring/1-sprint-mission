package com.sprint.mission.discodeit.validation;

import static com.sprint.mission.discodeit.util.MessageRegex.MESSAGE_REGEX;

import com.sprint.mission.discodeit.exception.notfound.ResourceNotFoundException;
import com.sprint.mission.discodeit.exception.validation.InvalidResourceException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageValidator {

  private final UserRepository userRepository;
  private final ChannelRepository channelRepository;

  public void validateUserId(UUID userId) {
    if (userRepository.findById(userId).isEmpty()) {
      throw new ResourceNotFoundException("User not found: " + userId);
    }
  }

  public void validateChannelId(UUID channelId) {
    if (channelRepository.findById(channelId).isEmpty()) {
      throw new ResourceNotFoundException("Channel not found: " + channelId);
    }
  }

  public void validateMessage(String message) {
    if (message == null || !message.matches(MESSAGE_REGEX)) {
      throw new InvalidResourceException("Invalid message: " + message);
    }
  }
}
