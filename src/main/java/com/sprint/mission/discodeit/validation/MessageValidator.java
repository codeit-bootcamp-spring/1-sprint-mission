package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionText;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.UUID;

public class MessageValidator {

    private final ChannelService channelService;
    private final UserService userService;

    public MessageValidator(ChannelService channelService, UserService userService) {
        this.channelService = channelService;
        this.userService = userService;
    }

    public boolean validateContent(String content) {
        if (content == null || content.trim().isEmpty()) {
            throw new CustomException(ExceptionText.INVALID_MESSAGE_CONTENT);
        }
        return true;
    }

    public boolean validateSender(UUID userId) {
        if (userService.getUser(userId) == null) {
            throw new CustomException(ExceptionText.USER_NOT_FOUND);
        }
        return true;
    }

    public boolean validateDestinationChannel(UUID channelId) {
        if (channelService.getChannel(channelId) == null) {
            throw new CustomException(ExceptionText.CHANNEL_NOT_FOUND);
        }
        return true;
    }

    public boolean validateMessage(User user, Channel channel, String content) {
        boolean isContentValid = validateContent(content);
        boolean isSenderValid = validateSender(user.getuuID());
        boolean isDestinationChannelValid = validateDestinationChannel(channel.getuuId());

        return isContentValid && isSenderValid && isDestinationChannelValid;
    }
}
