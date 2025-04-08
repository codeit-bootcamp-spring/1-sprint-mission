package com.sprint.mission.discodeit.exception.message;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.UUID;

public class MessageNotFoundException extends MessageException {
    public MessageNotFoundException(UUID id) {
        super(ErrorCode.MESSAGE_NOT_FOUND);
        this.getDetails().put("id", id);
    }
}
