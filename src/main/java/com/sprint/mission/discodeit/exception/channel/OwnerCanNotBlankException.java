package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class OwnerCanNotBlankException extends ChannelException {
    public OwnerCanNotBlankException() {
        super(ErrorCode.OWNER_CANNOT_BLANK);
    }
}
