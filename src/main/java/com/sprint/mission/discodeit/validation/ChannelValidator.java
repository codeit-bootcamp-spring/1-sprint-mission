package com.sprint.mission.discodeit.validation;

import static com.sprint.mission.discodeit.util.ChannelRegex.CHANNEL_NAME;
import static com.sprint.mission.discodeit.util.ChannelRegex.DESCRIPTION;

import com.sprint.mission.discodeit.exception.validation.InvalidResourceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelValidator {

  public void validatePublicChannel(String name, String description) {
    if (!name.matches(CHANNEL_NAME)) {
      throw new InvalidResourceException("Invalid channel name: " + name);
    }

    if (!description.matches(DESCRIPTION)) {
      throw new InvalidResourceException("Invalid channel description: " + description);
    }
  }
}
