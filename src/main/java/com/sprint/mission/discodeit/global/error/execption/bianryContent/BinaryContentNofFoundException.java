package com.sprint.mission.discodeit.global.error.execption.bianryContent;

import com.sprint.mission.discodeit.global.error.ErrorCode;
import com.sprint.mission.discodeit.global.error.execption.EntityNotFoundException;

public class BinaryContentNofFoundException extends EntityNotFoundException {
    public BinaryContentNofFoundException(String value) {
        super(ErrorCode.BINARYCONTENT_NOT_FOUND.format(value), ErrorCode.BINARYCONTENT_NOT_FOUND);
    }
}
