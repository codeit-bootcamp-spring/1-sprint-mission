package com.sprint.mission.discodeit.exception.binaryContent;

import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import lombok.NoArgsConstructor;
import java.util.HashMap;

@NoArgsConstructor
public class BinaryContentException extends DiscodeitException {
    public BinaryContentException(ErrorCode errorCode, HashMap<String, Object> details){
        super(errorCode, details);
    }
}
