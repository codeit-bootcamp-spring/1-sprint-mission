package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;

public class MessageException extends DiscodeitException {
    public MessageException(ErrorCode errorCode) {
        super(errorCode);
    }

    public MessageException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public static class MessageNotFoundException extends MessageException {
        public MessageNotFoundException() {
            super(ErrorCode.MESSAGE_NOT_FOUND);
        }

        public MessageNotFoundException(String message) {
            super(ErrorCode.MESSAGE_NOT_FOUND, message);
        }
    }

    public static class MessageUpdateDeniedException extends MessageException {
        public MessageUpdateDeniedException() {
            super(ErrorCode.MESSAGE_UPDATE_DENIED);
        }

        public MessageUpdateDeniedException(String message) {
            super(ErrorCode.MESSAGE_UPDATE_DENIED, message);
        }
    }

    public static class MessageDeleteDeniedException extends MessageException {
        public MessageDeleteDeniedException() {
            super(ErrorCode.MESSAGE_DELETE_DENIED);
        }

        public MessageDeleteDeniedException(String message) {
            super(ErrorCode.MESSAGE_DELETE_DENIED, message);
        }
    }
} 