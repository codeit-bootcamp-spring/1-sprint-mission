package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.jpa.MessageRepository;
import com.sprint.mission.discodeit.repository.jpa.ReadStatusRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelMapper {

  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;
  private final UserMapper userMapper;

  public ChannelDto toDto(Channel channel) {

    List<UserDto> participants = new ArrayList<>();
    if (channel.getChannelType().equals(ChannelType.PRIVATE)) {
      participants = readStatusRepository.findAllByChannel(channel)
          .stream()
          .map(ReadStatus::getUser)   // ReadStatus -> User
          .map(userMapper::toDto)     // User -> UserDto
          .toList();
    }

    Instant lastMessageAt = messageRepository.findAllByChannel(channel)
        .stream()
        .map(Message::getCreatedAt)
        .max(Comparator.naturalOrder())  // 가장 최신 메시지 찾기
        .orElse(Instant.MIN);

    return new ChannelDto(
        channel.getId(),
        channel.getChannelType(),
        channel.getName(),
        channel.getDescription(),
        participants,
        lastMessageAt
    );
  }
}
