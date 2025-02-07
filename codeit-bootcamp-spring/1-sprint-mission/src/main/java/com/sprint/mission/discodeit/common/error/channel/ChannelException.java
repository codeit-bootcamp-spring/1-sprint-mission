package com.sprint.mission.discodeit.common.error.channel;

import com.sprint.mission.discodeit.common.error.ErrorCode;
import java.util.UUID;

public class ChannelException extends RuntimeException {

    private ChannelException(String message) {
        super(message);
    }

    private ChannelException(String message, Throwable cause) {
        super(message, cause);
    }

    public static ChannelException of(ErrorCode message) {
        return new ChannelException(message.getErrorMessage());
    }

    public static ChannelException ofNotFound(ErrorCode message, UUID causeInputParameter) {
        var format =
                String.format(
                        "%s : input Channel Id = %s",
                        message.getErrorMessage(),
                        causeInputParameter.toString()
                );

        return new ChannelException(format);
    }

    public static ChannelException ofNotCreatorName(
            ErrorCode message,
            String creatorName
    ) {
        var format = String.format(
                "%s : 채널 생성자 %s 가 아닙니다.",
                message.getErrorMessage(), creatorName
        );

        return new ChannelException(format);
    }
}
