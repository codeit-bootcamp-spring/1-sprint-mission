package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Named("ChannelMapperHelper")
public class ChannelMapperHelper {

  UserRepository userRepository;
  MessageRepository messageRepository;
  ReadStatusRepository readStatusRepository;
  UserMapper userMapper;

  @Autowired
  public ChannelMapperHelper(UserRepository userRepository, MessageRepository messageRepository,
      ReadStatusRepository readStatusRepository,
      UserMapper userMapper) {
    this.userRepository = userRepository;
    this.messageRepository = messageRepository;
    this.readStatusRepository = readStatusRepository;
    this.userMapper = userMapper;
  }

  @Named("mapUserById")
  public List<UserDto> mapProfileById(List<UUID> profileIds) {
    List<UserDto> participants = new ArrayList<>();
    for (UUID id : profileIds) {
      participants.add(userRepository.findById(id)
          .map(userMapper::toDto)
          .orElse(null)
      );
    }
    return participants;
  }

//  public ChannelDto channelToDto(Channel channel) {
//    Instant lastMessageAt = messageRepository.findAllByChannelId(channel.getId())
//        .stream()
//        .sorted(Comparator.comparing(Message::getCreatedAt).reversed())
//        .map(Message::getCreatedAt)
//        .limit(1)
//        .findFirst()
//        .orElse(Instant.MIN);
//
//    List<UUID> participantIds = new ArrayList<>();
//    if (channel.getType().equals(ChannelType.PRIVATE)) {
//      readStatusRepository.findAllByChannelId(channel.getId())
//          .stream()
//          .map(ReadStatus::getUserId)
//          .forEach(participantIds::add);
//    }
//
//    return channelMapper.toDto(channel, participantIds, lastMessageAt);
//  }
}
