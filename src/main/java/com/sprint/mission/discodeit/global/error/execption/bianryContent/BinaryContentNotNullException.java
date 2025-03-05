package com.sprint.mission.discodeit.global.error.execption.bianryContent;

import com.sprint.mission.discodeit.global.error.ErrorCode;
import com.sprint.mission.discodeit.global.error.execption.InvalidValueException;

public class BinaryContentNotNullException extends InvalidValueException {

    public BinaryContentNotNullException() {
        super(ErrorCode.BINARYCONTENT_NOT_NULL.getMessage(), ErrorCode.BINARYCONTENT_NOT_NULL);
    }
}
