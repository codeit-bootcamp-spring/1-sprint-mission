package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.HashMap;

public class BinaryContentSaveFailedException extends BinaryContentException{
    public BinaryContentSaveFailedException(HashMap<String, Object> details){
        super(ErrorCode.BINARYCONTENT_SAVE_FAILED, details);
    }
}