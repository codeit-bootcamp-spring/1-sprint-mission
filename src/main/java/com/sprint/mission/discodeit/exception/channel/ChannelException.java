package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class ChannelException extends DiscodeitException {
    public ChannelException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ChannelException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public static class ChannelNotFoundException extends ChannelException {
        public ChannelNotFoundException() {
            super(ErrorCode.CHANNEL_NOT_FOUND);
        }

        public ChannelNotFoundException(String message) {
            super(ErrorCode.CHANNEL_NOT_FOUND, message);
        }
    }

    public static class PrivateChannelUpdateException extends ChannelException {
        public PrivateChannelUpdateException() {
            super(ErrorCode.PRIVATE_CHANNEL_UPDATE);
        }

        public PrivateChannelUpdateException(String message) {
            super(ErrorCode.PRIVATE_CHANNEL_UPDATE, message);
        }
    }

    public static class ChannelAccessDeniedException extends ChannelException {
        public ChannelAccessDeniedException() {
            super(ErrorCode.CHANNEL_ACCESS_DENIED);
        }

        public ChannelAccessDeniedException(String message) {
            super(ErrorCode.CHANNEL_ACCESS_DENIED, message);
        }
    }
} 