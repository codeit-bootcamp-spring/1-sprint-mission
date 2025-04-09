package com.sprint.mission.discodeit.validator;

import com.sprint.mission.discodeit.execption.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelValidator {

  private final ChannelRepository channelRepository;

  public Channel validateChannelExistsByChannelId(UUID channelId) {
    return channelRepository.findChannelById(channelId)
        .orElseThrow(() -> new ChannelNotFoundException(Map.of("channelId", channelId)));
  }
}
