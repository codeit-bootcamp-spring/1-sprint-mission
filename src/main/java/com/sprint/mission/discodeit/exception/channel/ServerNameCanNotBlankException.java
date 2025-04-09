package com.sprint.mission.discodeit.exception.channel;

import com.sprint.mission.discodeit.exception.ErrorCode;

public class ServerNameCanNotBlankException extends ChannelException {
    public ServerNameCanNotBlankException() {
        super(ErrorCode.SERVERNAME_CANNOT_BLANK);
    }
}
