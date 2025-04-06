package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.ErrorCode;

import java.util.HashMap;

public class DirectoryInitFailedException extends BinaryContentException{
    public DirectoryInitFailedException(HashMap<String, Object> details){
        super(ErrorCode.DIRECTORY_INIT_FAILED, details);
    }
}