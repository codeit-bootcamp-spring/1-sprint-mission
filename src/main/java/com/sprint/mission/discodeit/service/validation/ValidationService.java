package com.sprint.mission.discodeit.service.validation;

import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.UUID;

public class ValidationService {

    private final ChannelService channelService;
    private final UserService userService;

    public ValidationService(ChannelService channelService, UserService userService) {
        this.channelService = channelService;
        this.userService = userService;
    }

    public boolean isValidChannel(UUID channelId) {
        return channelService.getChannel(channelId) != null;
    }

    public boolean isValidUser(UUID userId) {
        return userService.getUser(userId) != null;
    }

    public boolean validate(UUID channelId, UUID userId) {
        return isValidChannel(channelId) && isValidUser(userId);
    }
}
