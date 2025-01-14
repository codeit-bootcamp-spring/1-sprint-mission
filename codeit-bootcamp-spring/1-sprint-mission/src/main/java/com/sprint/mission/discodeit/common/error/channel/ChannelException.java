package com.sprint.mission.discodeit.common.error.channel;

import com.sprint.mission.discodeit.common.error.ErrorMessage;

public class ChannelException extends RuntimeException {

    private ChannelException(String message) {
        super(message);
    }

    private ChannelException(String message, Throwable cause) {
        super(message, cause);
    }

    public static ChannelException of(ErrorMessage message) {
        return new ChannelException(message.getMessage());
    }

    public static ChannelException ofErrorMessageAndCreatorName(
            ErrorMessage message,
            String creatorName
    ) {
        var format = String.format(
                "%s : 채널 생성자 %s 가 아닙니다.",
                message.getMessage(), creatorName
        );

        return new ChannelException(format);
    }
}
