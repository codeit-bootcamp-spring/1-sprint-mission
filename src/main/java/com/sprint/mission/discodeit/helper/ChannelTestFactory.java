package com.sprint.mission.discodeit.helper;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.util.UUID;

public class ChannelTestFactory {

  public static Channel create(String name, String description) {
    return Channel.builder()
        .name(name)
        .description(description)
        .channelType(ChannelType.PUBLIC)
        .build();
  }

  public static Channel createPrivate() {
    return Channel.builder()
        .name(null)
        .description(null)
        .channelType(ChannelType.PRIVATE)
        .build();
  }

  public static Channel createRandom() {
    return Channel.builder()
        .name("channel_" + UUID.randomUUID())
        .description("설명")
        .channelType(ChannelType.PUBLIC)
        .build();
  }
}