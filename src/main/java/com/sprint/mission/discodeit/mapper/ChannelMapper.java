package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class ChannelMapper {

  MessageRepository messageRepository;
  ReadStatusRepository readStatusRepository;
  UserMapper userMapper;

  public ChannelDto toDto(Channel channel) {
    if (channel == null) {
      return null;
    }
    Optional<Instant> first = messageRepository.findAll().stream().map(Message::getUpdatedAt)
        .min(Comparator.reverseOrder());
    List<UserDto> users = readStatusRepository.findAllByChannelId(channel.getId()).stream()
        .map(s -> UserMapper.toDto(s.getUser())).toList();
    return new ChannelDto(channel.getId(), channel.getType(), channel.getName(),
        channel.getDescription(), users, first.orElse(null)); //null처리 맞는 지 확인
  }
}
