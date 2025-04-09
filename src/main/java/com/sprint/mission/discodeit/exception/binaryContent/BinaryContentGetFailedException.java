package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.HashMap;

public class BinaryContentGetFailedException extends BinaryContentException{
    public BinaryContentGetFailedException(HashMap<String, Object> details){
        super(ErrorCode.BINARYCONTENT_GET_FAILED, details);
    }
}