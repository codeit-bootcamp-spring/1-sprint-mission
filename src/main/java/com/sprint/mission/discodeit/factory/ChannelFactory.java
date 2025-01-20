package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.entity.ChatChannel;
import com.sprint.mission.discodeit.entity.VoiceChannel;
import com.sprint.mission.discodeit.exception.ChannelValidationException;
import com.sprint.mission.discodeit.service.MessageServiceV2;
import com.sprint.mission.discodeit.service.UserService;

public class ChannelFactory {

//    private final UserService userService;
//    private final MessageServiceV2 messageServiceV2;
//
//    public ChannelFactory(UserService userService, MessageServiceV2 messageServiceV2) {
//        this.userService = userService;
//        this.messageServiceV2 = messageServiceV2;
//    }


  public ChatChannel createChatChannel(
      String serverUUID,
      String categoryUUID,
      String channelName,
      int maxNumberOfPeople
  ) throws ChannelValidationException {

    return new ChatChannel.ChatChannelBuilder()
        .serverUUID(serverUUID)
        .categoryUUID(categoryUUID)
        .channelName(channelName)
        .maxNumberOfPeople(maxNumberOfPeople)
        .isPrivate(true).build();

  }

  public VoiceChannel createVoiceChannel(
      String serverUUID,
      String categoryUUID,
      String channelName,
      boolean isPrivate) throws ChannelValidationException {
    return new VoiceChannel.VoiceChannelBuilder()
        .serverUUID(serverUUID)
        .categoryUUID(categoryUUID)
        .channelName(channelName)
        .isPrivate(isPrivate)
        .build();
  }
}
