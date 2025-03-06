package com.sprint.mission.discodeit.converter;

import com.sprint.mission.discodeit.dto.response.channel.ChannelResponseDTO;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.interfacepac.ReadStatusRepository;
import com.sprint.mission.discodeit.service.interfacepac.ChannelService;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelResponseConverter {

  private final ReadStatusRepository readStatusRepository;

  public List<ChannelResponseDTO> convertChannels(List<Channel> channels, ChannelType type) {
    return channels.stream()
        .map(channel -> new ChannelResponseDTO(
            channel.getId(),
            type == ChannelType.PUBLIC ? channel.getName() : null,
            type == ChannelType.PUBLIC ? channel.getDescription() : null,
            type,
            type == ChannelType.PRIVATE ? readStatusRepository.findUserIdsByChannel(channel) : null,
            channel.getLastMessageAt() != null ? channel.getLastMessageAt() : Instant.EPOCH
        )).toList();
  }
}
