package com.sprint.mission.discodeit.helper;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import java.util.UUID;

public class MessageTestFactory {

  public static Message create(String content, User author, Channel channel) {
    return Message.builder()
        .content(content)
        .author(author)
        .channel(channel)
        .build();
  }

  public static Message createRandom(User author, Channel channel) {
    return Message.builder()
        .content("message_" + UUID.randomUUID())
        .author(author)
        .channel(channel)
        .build();
  }
}