package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class ChannelDto {

  private UUID id;
  private ChannelType type;
  private String name;
  private String description;
  private List<UserDto> participants;// PRIVATE 채널 User ID 리스트
  private Instant lastMessageAt; // 가장 최근 메시지 시간

  public ChannelDto(Channel channel, Instant lastMessageAt, List<UserDto> privateUserIdList) {
    this.id = channel.getId();
    this.type = channel.getType();
    this.name = channel.getChannelName();
    this.description = channel.getDescription();
    this.lastMessageAt = lastMessageAt;
    this.participants = privateUserIdList;
  }

  //TODO: 다른 곳으로? 채널에 유저가 있는지 반환
  public Boolean isUserExist(UUID userId) {
    if (participants.contains(userId)) {
      return true;
    }
    return false;
  }
}
