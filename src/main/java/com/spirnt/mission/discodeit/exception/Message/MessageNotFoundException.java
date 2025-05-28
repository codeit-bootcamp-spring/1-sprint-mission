package com.spirnt.mission.discodeit.exception.Message;

import com.spirnt.mission.discodeit.exception.ErrorCode;
import java.util.Map;

/**
 * ID로 조회했는데 Message가 존재하지 않는 경우 발생하는 예외
 */
public class MessageNotFoundException extends MessageException {

    public MessageNotFoundException(Map<String, Object> details) {
        super(ErrorCode.MESSAGE_NOT_FOUND, details);
    }
}
