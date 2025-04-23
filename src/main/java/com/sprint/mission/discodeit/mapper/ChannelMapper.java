package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

// Channel의 participants 필드 변환 시 UserMapper 사용
@Mapper(componentModel = "spring", uses = {UserMapper.class})
public abstract class ChannelMapper {

  // 의존성 필드 주입
  @Autowired
  private MessageRepository messageRepository;
  @Autowired
  private ReadStatusRepository readStatusRepository;
  @Autowired
  private UserMapper userMapper;

  @Mapping(target = "participants", expression = "java(resolveParticipants(channel))")
  @Mapping(target = "lastMessageAt", expression = "java(resolveLastMessageAt(channel))")
  abstract public ChannelDto toDto(Channel channel);

  // 해당 채널 마지막 메시지 시간 반환(null일 경우 Instant 최소값 반환)
  protected Instant resolveLastMessageAt(Channel channel) {
    return messageRepository.findLastMessageAtByChannelId(channel.getId())
        .orElse(Instant.MIN);
  }

  // 비공개 채널일 경우 채널 참여자 반환
  protected List<UserDto> resolveParticipants(Channel channel) {
    List<UserDto> participants = new ArrayList<>();
    if (channel.getType().equals(ChannelType.PRIVATE)) {
      // 읽음 상태 통해 유저 목록 추출
      readStatusRepository.findAllByChannelIdWithUser(channel.getId()).stream()
          .map(ReadStatus::getUser)
          .map(userMapper::toDto)
          .forEach(participants::add);
    }
    return participants;
  }
}