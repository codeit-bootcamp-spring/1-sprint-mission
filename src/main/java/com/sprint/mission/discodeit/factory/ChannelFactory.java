package com.sprint.mission.discodeit.factory;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChatBehavior;
import com.sprint.mission.discodeit.entity.ChatBehaviorV2;
import com.sprint.mission.discodeit.entity.VoiceBehavior;
import com.sprint.mission.discodeit.exception.ChannelValidationException;
import com.sprint.mission.discodeit.service.MessageServiceV2;
import com.sprint.mission.discodeit.service.UserService;

public class ChannelFactory {

    private final UserService userService;
    private final MessageServiceV2 messageServiceV2;

    public ChannelFactory(UserService userService, MessageServiceV2 messageServiceV2) {
        this.userService = userService;
        this.messageServiceV2 = messageServiceV2;
    }

    public Channel createChatChannel(
        String serverUUID,
        String categoryUUID,
        String channelName,
        int maxNumberOfPeople
    ) throws ChannelValidationException {

        return new Channel.ChannelBuilder(serverUUID, categoryUUID, channelName, new ChatBehaviorV2(userService, messageServiceV2))
            .maxNumberOfPeople(maxNumberOfPeople)
            .isPrivate(true).build();

    }

    public Channel createVoiceChannel(
        String serverUUID,
        String categoryUUID,
        String channelName,
        boolean isPrivate) throws ChannelValidationException {
        return new Channel.ChannelBuilder(serverUUID, categoryUUID, channelName, new VoiceBehavior())
            .isPrivate(isPrivate)
            .build();
    }
}
