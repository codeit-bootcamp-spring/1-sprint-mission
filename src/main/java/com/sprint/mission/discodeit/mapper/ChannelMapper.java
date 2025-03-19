package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.request.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ChannelMapper {

  private final MessageRepository messageRepository;
  private final ReadStatusRepository readStatusRepository;
  private final UserRepository userRepository;
  private final UserMapper userMapper;

  public ChannelDto toPublicDto(Channel channel) {
    if (channel == null) {
      return null;
    }
    Optional<Instant> first = messageRepository.findAll().stream().map(Message::getUpdatedAt)
        .min(Comparator.reverseOrder());
    List<UserDto> users = readStatusRepository.findReadStatusesByChannelId(channel.getId()).stream()
        .map(s -> userMapper.toDto(s.getUser())).toList();
    return new ChannelDto(channel.getId(), channel.getType(), channel.getName(),
        channel.getDescription(), users, first.orElse(null)); //null처리 맞는 지 확인
  }

  public ChannelDto toPrivateDto(Channel channel) {
    if (channel == null) { return null; }
    Optional<Instant> first = messageRepository.findAll().stream().map(Message::getUpdatedAt)
        .min(Comparator.reverseOrder());
    System.out.println("first = " + first.orElse(null));
    //List<ReadStatus> readStatuses = readStatusRepository.findAll();
    //System.out.println("readStatuses = " + readStatuses);
    //List<ReadStatus> readStatusesByChannelId = readStatusRepository.findReadStatusesByChannelId(        channel.getId());
    //System.out.println("readStatusesByChannelId = " + readStatusesByChannelId);
    //List<UserDto> users = readStatusRepository.findReadStatusesByChannelId(channel.getId()).stream()        .map(s -> userMapper.toDto(s.getUser())).toList();
    List<User> users = userRepository.findAll();
    System.out.println("users = " + users);
    List<UserDto> list = new ArrayList<>();
    for (User user : users) {
      UserDto dto = userMapper.toDto(user);
      list.add(dto);
    }
    return new ChannelDto(channel.getId(), channel.getType(), channel.getName(),
        channel.getDescription(), list, first.orElse(null)); //null처리 맞는 지 확인
  }


}
