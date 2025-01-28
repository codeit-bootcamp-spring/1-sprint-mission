package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.entity.ChatChannel;
import com.sprint.mission.discodeit.entity.VoiceChannel;
import com.sprint.mission.discodeit.exception.ChannelValidationException;
import com.sprint.mission.discodeit.service.MessageServiceV2;
import com.sprint.mission.discodeit.service.UserService;

public class ChannelFactory {

  public ChatChannel createChatChannel(
      String serverUUID,
      String categoryUUID,
      String channelName,
      int maxNumberOfPeople
  ) throws ChannelValidationException {

    return new ChatChannel.ChatChannelBuilder(channelName)
        .serverUUID(serverUUID)
        .categoryUUID(categoryUUID)
        .maxNumberOfPeople(maxNumberOfPeople)
        .isPrivate(true).build();

  }

  public VoiceChannel createVoiceChannel(
      String serverUUID,
      String categoryUUID,
      String channelName,
      boolean isPrivate) throws ChannelValidationException {
    return new VoiceChannel.VoiceChannelBuilder(channelName)
        .serverUUID(serverUUID)
        .categoryUUID(categoryUUID)
        .isPrivate(isPrivate)
        .build();
  }
}
