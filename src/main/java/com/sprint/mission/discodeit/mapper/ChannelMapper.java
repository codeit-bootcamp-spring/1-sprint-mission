package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelMapper {

  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;
  private final UserMapper userMapper;

  public ChannelDto toDto(Channel channel) {
    Message message = messageRepository.findLatestMessageByChannelId(channel.getId()).orElse(null);
    Instant lastMessageTimeStamp = null;

    if (message != null) {
      lastMessageTimeStamp = message.getCreatedAt();
    }

    return new ChannelDto(
        channel.getId(),
        channel.getName(),
        channel.getType(),
        channel.getChannelCategory(),
        channel.getDescription(),
        channel.getCreatedAt(),
        lastMessageTimeStamp,
        channel.getUsers().stream().map(rs -> userMapper.toDto(rs.getUser())).toList()
    );
  }
}
