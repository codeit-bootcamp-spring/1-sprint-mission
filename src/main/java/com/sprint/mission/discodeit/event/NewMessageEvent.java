package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.entity.Notification.Type;

public record NewMessageEvent(
    Type type,
    MessageDto messageDto,
    ChannelDto channelDto
) {
  public static NewMessageEvent of(MessageDto messageDto, ChannelDto channelDto) {
    return new NewMessageEvent(Type.NEW_MESSAGE, messageDto, channelDto);
  }

}
