package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChannelMapper {

  private final UserRepository userRepository;
  private final ReadStatusRepository readStatusRepository;
  private final UserMapper userMapper;

  public ChannelDto toDto(Channel channel) {
    return ChannelDto.builder()
        .id(channel.getId())
        .type(channel.getType())
        .name(channel.getChannelName())
        .description(channel.getDescription())
        .participants(getUserDtoList(channel))
        .lastMessageAt(getLastMessageAt(channel))
        .build();
  }


  //Public 일 때 userIdList , time은 null
  private Instant getLastMessageAt(Channel channel) {
    if (channel.getType() == ChannelType.PRIVATE) {
      return readStatusRepository.findLatestTimeByChannelId(channel.getId());
    } else {
      return null;
    }
  }

  private List<UserDto> getUserDtoList(Channel channel) {
    //TODO: userDto를 이용해서 반환하기.
    if (channel.getType() == ChannelType.PRIVATE) {
      List<User> userList = readStatusRepository.findAllUserIdByChannelId(channel.getId()).stream()
          .map(userRepository::findById)
          .map(opt -> opt.orElse(null))
          .collect(Collectors.toList());

      return userList.stream()
          .map(userMapper::toDto) // User -> UserDto 변환
          .collect(Collectors.toList());

    } else {
      return null;
    }
  }


}
