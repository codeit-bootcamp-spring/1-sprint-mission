package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionText;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.stereotype.Component;

import java.util.UUID;
@Component
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

    public boolean validateSender(UUID uuid) {
        if (userService.getUser(uuid) == null) {
            throw new CustomException(ExceptionText.USER_NOT_FOUND);
        }
        return true;
    }

    public boolean validateDestinationChannel(UUID uuid) {
        if (channelService.find(uuid) == null) {
            throw new CustomException(ExceptionText.CHANNEL_NOT_FOUND);
        }
        return true;
    }

    public boolean validateMessage(User user, Channel channel, String content) {
        boolean isContentValid = validateContent(content);
        boolean isSenderValid = validateSender(user.getId());
        boolean isDestinationChannelValid = validateDestinationChannel(channel.getId());

        return isContentValid && isSenderValid && isDestinationChannelValid;
    }
}
