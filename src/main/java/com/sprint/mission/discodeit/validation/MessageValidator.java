package com.sprint.mission.discodeit.validation;

import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ExceptionCode;
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

    public boolean checkChannelAndUser(UUID channelId, UUID userId) {
        if (channelService.getChannel(channelId) == null) {
            throw new CustomException(ExceptionCode.POST_NOT_FOUND); // 채널을 찾을 수 없다는 예외
        }
        if (userService.getUser(userId) == null) {
            throw new CustomException(ExceptionCode.USER_NOT_FOUND); // 사용자를 찾을 수 없다는 예외
        }

        return true;
    }
}
