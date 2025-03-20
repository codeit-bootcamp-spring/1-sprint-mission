package com.sprint.mission.discodeit.mapper.entitymapper;

import com.sprint.mission.discodeit.dto.response.ChannelDto;
import com.sprint.mission.discodeit.dto.response.UserDto;
import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelMapper {

  private final ReadStatusRepository readStatusRepository;

  public ChannelDto toDto(Channel channel) {
    if (channel == null) {
      throw new ChannelNotFoundException();
    }

    List<UserDto> users = readStatusRepository.findAllByChannel(channel).stream()
        .map(st -> UserMapper.toDto(st.getUser()))
        .toList();

    return ChannelDto.builder()
        .id(channel.getId())
        .type(channel.getType())
        .name(channel.getName())
        .description(channel.getDescription())
        .participants(users)
        .lastMessageAt(
            readStatusRepository.findFirstByChannelOrderByLastReadAtDesc(channel).getLastReadAt())
        .build();
  }
}
