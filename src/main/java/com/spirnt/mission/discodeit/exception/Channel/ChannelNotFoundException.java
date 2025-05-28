package com.spirnt.mission.discodeit.exception.Channel;

import com.spirnt.mission.discodeit.exception.ErrorCode;
import java.util.Map;

/**
 * ID로 조회했는데 Channel이 존재하지 않는 경우 발생하는 예외
 */
public class ChannelNotFoundException extends ChannelException {

    public ChannelNotFoundException(Map<String, Object> details) {
        super(ErrorCode.CHANNEL_NOT_FOUND, details);
    }
}
