package com.sprint.mission.discodeit.entity.channel.add;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.UUID;

@Getter
@AllArgsConstructor
public class ChannelAddUserResponse {

  private UUID channelID;
  private String channelName;
  private String userName;
}
