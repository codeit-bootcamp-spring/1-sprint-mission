package com.spirnt.mission.discodeit.exception.ReadStatus;

import com.spirnt.mission.discodeit.exception.ErrorCode;
import java.util.Map;

/**
 * ID로 조회했는데 ReadStatus가 존재하지 않는 경우 발생하는 예외
 */
public class ReadStatusNotFoundException extends ReadStatusException {

    public ReadStatusNotFoundException(Map<String, Object> details) {
        super(ErrorCode.READSTATUS_NOT_FOUND, details);
    }
}
