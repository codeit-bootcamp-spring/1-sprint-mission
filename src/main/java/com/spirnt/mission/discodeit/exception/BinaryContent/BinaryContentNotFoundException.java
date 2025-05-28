package com.spirnt.mission.discodeit.exception.BinaryContent;

import com.spirnt.mission.discodeit.exception.ErrorCode;
import java.util.Map;

/**
 * ID로 조회했는데 BinaryContent가 존재하지 않는 경우 발생하는 예외
 */
public class BinaryContentNotFoundException extends BinaryContentException {

    public BinaryContentNotFoundException(Map<String, Object> details) {
        super(ErrorCode.BINARYCONTENT_NOT_FOUND, details);
    }
}
