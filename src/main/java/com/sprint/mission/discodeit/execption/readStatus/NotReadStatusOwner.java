package com.sprint.mission.discodeit.execption.readStatus;

import com.sprint.mission.discodeit.execption.ErrorCode;
import java.util.Map;

public class NotReadStatusOwner extends ReadStatusException {

    public NotReadStatusOwner(Map<String, Object> details) {
        super(ErrorCode.NOT_READSTATUS_OWNER, details);
    }

}
