package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.ErrorCode;
import java.util.HashMap;

public class BinaryContentNotFoundException extends BinaryContentException{
    public BinaryContentNotFoundException(HashMap<String, Object> details){
        super(ErrorCode.BINARYCONTENT_NOT_FOUND, details);
    }
}
