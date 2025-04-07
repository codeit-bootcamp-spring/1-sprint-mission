package com.sprint.mission.discodeit.exception.readStatus;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.UUID;

public class ReadStatusAlreadyExistsException extends ReadStatusException{
    public ReadStatusAlreadyExistsException(UUID userId, UUID channelId) {
        super(ErrorCode.READ_STATUS_ALREADY_EXISTS);
        this.getDetails().put("userId", userId);
        this.getDetails().put("channelId", channelId);
    }
}
