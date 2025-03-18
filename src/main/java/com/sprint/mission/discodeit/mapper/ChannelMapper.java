package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public abstract class ChannelMapper {

  @Autowired
  private MessageRepository messageRepository;
  @Autowired
  private ReadStatusRepository readStatusRepository;

  @Autowired
  private UserMapper userMapper;

  @Mapping(target = "participants", expression = "java(resolveParticipants(channel))")
  @Mapping(target = "lastMessageAt", expression = "java(resolveLastMessageAt(channel))")
  abstract public ChannelDto toDto(Channel channel);


  protected Instant resolveLastMessageAt(Channel channel) {
    return messageRepository.findByChannelId(channel.getId())
        .stream()
        .max(Comparator.comparing(BaseEntity::getCreatedAt))
        .map(BaseEntity::getCreatedAt)
        .orElse(Instant.MIN);
  }

  protected List<UserDto> resolveParticipants(Channel channel) {
    return readStatusRepository.findByChannelId(channel.getId())
        .stream()
        .map(ReadStatus::getUser)
        .map(userMapper::toDto)
        .toList();
  }
}
